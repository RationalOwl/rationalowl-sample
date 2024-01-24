import Foundation

import INI
import RationalOwl

class Config
{
    static let shared: Config! = Config()
    
    let roGateHost, roSvcId: String
    let umsHost, umsAccountId: String
    let umsMsgRetainDay: Int
    
    private init?()
    {
        do
        {
            guard let path = Bundle.main.path(forResource: "config", ofType: "ini") else {
                return nil
            }
            
            let config = try parseINI(filename: path)
            let rationalOwl = config["rational_owl"]
            
            guard let roGateHost = rationalOwl?["gate_host"] else
            {
                print("gate_host should be set!!")
                exit(1)
                return nil
            }
            
            guard let roSvcId = rationalOwl?["svc_id"] else
            {
                print("svc_id should be set!!")
                exit(1)
                return nil
            }
            
            let ums = config["ums"]
            
            guard let umsHost = ums?["ums_host"] else
            {
                print("ums_host should be set!!")
                exit(1)
                return nil
            }
            
            guard let umsAccountId = ums?["account_id"] else
            {
                print("account_id should be set!!")
                exit(1)
                return nil
            }
            
            guard let umsMsgRetainDayStr = ums?["msg_retain_day"] else
            {
                print("msg_retain_day should be set!!")
                exit(1)
                return nil
            }
            
            self.roGateHost = roGateHost
            self.roSvcId = roSvcId
            self.umsHost = umsHost
            self.umsAccountId = umsAccountId
            self.umsMsgRetainDay = Int(umsMsgRetainDayStr)!
        }
        catch {
            print(error)
            return nil
        }
    }
}
