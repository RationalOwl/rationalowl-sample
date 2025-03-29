import Foundation

class UmsApi {

    private static let UMS_REST_SERVER = "https://dev.rationalowl.com:36001"
    private static let httpClient = URLSession.shared

    // 설치 API 호출
    static func callInstallUmsApp(accountId: String, deviceRegId: String, phoneNum: String?, appUserId: String?, name: String?, completion: @escaping (Data?, URLResponse?, Error?) -> Void) {
        print("UmsApi: callInstallUmsApp enter")

        let urlString = "\(UMS_REST_SERVER)/pushApp/installApp"
        guard let url = URL(string: urlString) else { return }

        var req = PushAppProto.PushAppInstallReq()
        req.dt = UmsProtocol.APP_TYPE_ANDROID
        req.aId = accountId
        req.dRId = deviceRegId
        req.pn = phoneNum ?? ""
        req.auId = appUserId ?? ""
        req.n = name ?? ""

        do {
            let postBody = try JSONEncoder().encode(req)
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
            request.httpBody = postBody

            let task = httpClient.dataTask(with: request, completionHandler: completion)
            task.resume()
        } 
        catch {
            print("Error encoding request: \(error)")
        }
    }

    // 등록 해제 API 호출
    static func callUnregisterUmsApp(accountId: String, deviceRegId: String, completion: @escaping (Data?, URLResponse?, Error?) -> Void) {
        print("UmsApi: callUnregisterUmsApp enter")

        let urlString = "\(UMS_REST_SERVER)/pushApp/unregUser"
        guard let url = URL(string: urlString) else { return }

        var req = PushAppProto.PushAppUnregUserReq()
        req.aId = accountId
        req.dRId = deviceRegId

        do {
            let postBody = try JSONEncoder().encode(req)
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
            request.httpBody = postBody

            let task = httpClient.dataTask(with: request, completionHandler: completion)
            task.resume()
        } 
        catch {
            print("Error encoding request: \(error)")
        }
    }
}