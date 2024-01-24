import Foundation

class MessageReadReceiptService
{
    static let shared = MessageReadReceiptService()
    
    private let encoder: JSONEncoder
    private let decoder: JSONDecoder
    private let repository = MessagesRepository.shared
    
    private init()
    {
        encoder = JSONEncoder()
        encoder.dateEncodingStrategy = .millisecondsSince1970
        
        decoder = JSONDecoder()
        decoder.dateDecodingStrategy = .millisecondsSince1970
    }
    
    func markAsRead(_ messageId: String)
    {
        NSLog("[\(type(of: self))] markAsRead(\(messageId))")
        
        sendReadRequest(messageId)
        repository.setAsRead(messageId)
    }
    
    private func sendReadRequest(_ messageId: String)
    {
        NSLog("[\(type(of: self))] sendReadRequest(\(messageId))")
        
        let user: User = UserRepository.shared.user!
        let request = MessageReadRequest(phoneNumber: user.phoneNumber,
                                         deviceRegId: user.regId,
                                         messageId: messageId)
        
        UmsService.shared.setMessageRead(request) { [weak self] result in
            guard let self = self else { return }
            
            switch result
            {
            case .success(let response):
                NSLog("[\(type(of: self))] Result code: \(response)")
            case .failure(let error):
                NSLog("[\(type(of: self))] Error: \(error)")
            }
        }
    }
}
