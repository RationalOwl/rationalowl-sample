import Foundation

import Alamofire

class MessageRepository {
    static let shared = MessageRepository()
    
    func getImage(_ message: DataDef.Message, completion: @escaping (Result<Data, AFError>) -> (Void)) {
        guard let imageId = message.imageId else { return }
        
        let request = PushAppProto.PushAppImgDataReq(
            mAccountId: Config.shared.umsAccountId,
            mImgId: imageId,
            mMsgId: message.id
        )
        
        UmsService.shared.getImageData(request) { [weak self] result in
            guard let self = self else { return }
            
            switch result {
            case let .success(response):
                guard response.mResultCode == UmsResult.RESULT_OK else {
                    NSLog("[\(type(of: self))] [\(response.mResultCode)] \(String(describing: response.mComment))")
                    return
                }
                
                guard let imageData = Data(base64Encoded: response.mImgData) else { return }
                completion(.success(imageData))
            case let .failure(error):
                NSLog("[\(type(of: self))] \(error)")
                
                completion(.failure(error))
                return
            }
        }
    }
    
    func getDeliveryInfo(_ message: DataDef.Message, completion: @escaping (Result<DataDef.Message, AFError>) -> (Void)) {
        let diffDays: Int = Calendar.current.dateComponents([.day], from: message.sentAt, to: Date()).day ?? 0
        
        if diffDays >= 7 {
            completion(.success(message))
            return
        }
        
        let user = UserLocalDataSource.shared.user!
        
        let request = PushAppProto.PushAppMsgInfoReq(
            mAccountId: Config.shared.umsAccountId,
            mPhoneNum: user.phoneNumber,
            mDeviceRegId: user.regId,
            mMsgId: message.id
        )
        
        UmsService.shared.getMessageInfo(request) { [weak self] result in
            guard let self = self else { return }
            
            switch result {
            case let .success(response):
                guard response.mResultCode == UmsResult.RESULT_OK else {
                    NSLog("[\(type(of: self))] [\(response.mResultCode)] \(String(describing: response.mComment))")
                    return
                }
                
                let newMessage = MessageLocalDataSource.shared.updateDeliveryInfo(message.id,
                                                                                  mAlimtalkSendTime: response.mAlimtalkSendTime,
                                                                                  mMunjaSendTime: response.mMunjaSendTime,
                                                                                  mMunjaType: response.mMunjaType)!
                completion(.success(newMessage))
            case let .failure(error):
                NSLog("[\(type(of: self))] \(error)")
                completion(.failure(error))
            }
        }
    }
    
    func markAsRead(_ message: DataDef.Message) {
        sendReadRequest(message)
        MessageLocalDataSource.shared.markAsRead(message.id)
    }
    
    func markAllAsRead() {
        for message in MessageLocalDataSource.shared.messages.value {
            sendReadRequest(message)
        }
        
        MessageLocalDataSource.shared.markAllAsRead()
    }
    
    func sendReadRequest(_ message: DataDef.Message) {
        let user = UserLocalDataSource.shared.user!
        let request = PushAppProto.PushAppMsgReadNoti(mAccountId: Config.shared.umsAccountId,
                                                      mPhoneNum: user.phoneNumber,
                                                      mDeviceRegId: user.regId,
                                                      mMsgId: message.id)
        
        UmsService.shared.setMessageRead(request) { [weak self] result in
            guard let self = self else { return }
            
            if case .failure(let error) = result {
                NSLog("[\(type(of: self))] \(error)")
            }
        }
    }
}
