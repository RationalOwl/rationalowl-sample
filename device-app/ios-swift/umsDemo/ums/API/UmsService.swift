import Alamofire
import Foundation

class UmsService {
    private static let UMS_PORT = 36001

    static let shared = UmsService(host: Config.shared.umsHost, port: UMS_PORT)

    private let baseUrl: String

    private let parameterEncoder: JSONParameterEncoder
    private let decoder: JSONDecoder

    init(host: String, port: Int) {
        baseUrl = "\(host):\(port)/"

        let encoder = JSONEncoder()
        encoder.dateEncodingStrategy = .millisecondsSince1970
        parameterEncoder = JSONParameterEncoder(encoder: encoder)

        decoder = JSONDecoder()
        decoder.dateDecodingStrategy = .millisecondsSince1970
    }

    func requestAuthNumber(_ request: PushAppProto.PushAppAuthNumberReq,
                           completion: @escaping (Swift.Result<PushAppProto.PushAppAuthNumberRes, AFError>) -> Void)
    {
        AF.request(baseUrl + "pushApp/reqAuthNumber",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseDecodable(of: PushAppProto.PushAppAuthNumberRes.self) { response in
                completion(response.result)
            }
    }

    func verifyAuthNumber(_ request: PushAppProto.PushAppVerifyAuthNumberReq,
                          completion: @escaping (Swift.Result<PushAppProto.PushAppVerifyAuthNumberRes, AFError>) -> Void)
    {
        AF.request(baseUrl + "pushApp/verifyAuthNumber",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseDecodable(of: PushAppProto.PushAppVerifyAuthNumberRes.self) { response in
                completion(response.result)
            }
    }

    func signUp(_ request: PushAppProto.PushAppInstallReq,
                completion: @escaping (Swift.Result<PushAppProto.PushAppInstallRes, AFError>) -> Void)
    {
        AF.request(baseUrl + "pushApp/installApp",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseDecodable(of: PushAppProto.PushAppInstallRes.self) { response in
                completion(response.result)
            }
    }

    func getMessageInfo(_ request: PushAppProto.PushAppMsgInfoReq,
                        completion: @escaping (Swift.Result<PushAppProto.PushAppMsgInfoRes, AFError>) -> Void)
    {
        AF.request(baseUrl + "pushApp/msgInfo",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseDecodable(of: PushAppProto.PushAppMsgInfoRes.self) { response in
                completion(response.result)
            }
    }

    func getImageData(_ request: PushAppProto.PushAppImgDataReq,
                      completion: @escaping (Swift.Result<PushAppProto.PushAppImgDataRes, AFError>) -> Void)
    {
        AF.request(baseUrl + "pushApp/imgData",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseDecodable(of: PushAppProto.PushAppImgDataRes.self) { response in
                completion(response.result)
            }
    }

    func setMessageRead(_ request: PushAppProto.PushAppMsgReadNoti,
                        completion: @escaping (Swift.Result<String, AFError>) -> Void)
    {
        AF.request(baseUrl + "pushApp/notiRead",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseString { response in
                completion(response.result)
            }
    }

    func deleteUser(_ request: PushAppProto.PushAppUnregUserReq,
                    completion: @escaping (Swift.Result<PushAppProto.PushAppUnregUserRes, AFError>) -> Void)
    {
        AF.request(baseUrl + "pushApp/unregUser",
                   method: .post,
                   parameters: request,
                   encoder: parameterEncoder)
            .responseDecodable(of: PushAppProto.PushAppUnregUserRes.self) { response in
                completion(response.result)
            }
    }
}
