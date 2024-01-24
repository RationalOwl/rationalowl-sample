import Foundation
import Alamofire

struct UmsService
{
    private static let baseURL = Config.shared.umsHost + ":36001"

    static let shared = UmsService()
    
    private let parameterEncoder: JSONParameterEncoder
    private let decoder: JSONDecoder
    
    init()
    {
        let encoder = JSONEncoder()
        encoder.dateEncodingStrategy = .millisecondsSince1970
        parameterEncoder = JSONParameterEncoder(encoder: encoder)
        
        decoder = JSONDecoder()
        decoder.dateDecodingStrategy = .millisecondsSince1970
    }
    
    func requestAuthNumber(_ request: AuthNumberRequest, completion: @escaping (Swift.Result<AuthNumberResponse, AFError>) -> Void)
    {
        AF.request(UmsService.baseURL + "/pushApp/reqAuthNumber",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseDecodable(of: AuthNumberResponse.self) { response in
                completion(response.result)
            }
    }
    
    func verifyAuthNumber(_ request: VerifyAuthNumberRequest, completion: @escaping (Swift.Result<VerifyAuthNumberResponse, AFError>) -> Void) {
        AF.request(UmsService.baseURL + "/pushApp/verifyAuthNumber",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseDecodable(of: VerifyAuthNumberResponse.self) { response in
                completion(response.result)
            }
    }
    
    func signUp(_ request: SignUpRequest, completion: @escaping (Swift.Result<SignUpResponse, AFError>) -> Void) {
        AF.request(UmsService.baseURL + "/pushApp/installApp",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseDecodable(of: SignUpResponse.self) { response in
                completion(response.result)
            }
    }
    
    func getMessageInfo(_ request: MessageInfoRequest, completion: @escaping (Swift.Result<MessageInfoResponse, AFError>) -> Void) {
        AF.request(UmsService.baseURL + "/pushApp/msgInfo",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseDecodable(of: MessageInfoResponse.self) { response in
                completion(response.result)
            }
    }
    
    func getImageData(_ request: ImageDataRequest, completion: @escaping (Swift.Result<ImageDataResponse, AFError>) -> Void) {
        AF.request(UmsService.baseURL + "/pushApp/imgData",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseDecodable(of: ImageDataResponse.self) { response in
                completion(response.result)
            }
    }
    
    func setMessageRead(_ request: MessageReadRequest, completion: @escaping (Swift.Result<String, AFError>) -> Void) {
        AF.request(UmsService.baseURL + "/pushApp/notiRead",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseString { response in
                completion(response.result)
            }
    }
    
    func deleteUser(_ request: DeleteUserRequest, completion: @escaping (Swift.Result<DeleteUserResponse, AFError>) -> Void) {
        AF.request(UmsService.baseURL + "/pushApp/unregUser",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseDecodable(of: DeleteUserResponse.self) { response in
                completion(response.result)
            }
    }
}
