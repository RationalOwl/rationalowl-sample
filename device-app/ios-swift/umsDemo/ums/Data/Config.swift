import Foundation

import INI
import RationalOwl

class Config {
    static let shared: Config! = Config()
    
    /* rationalowl config */
    let roGateHost, roSvcId: String
    /* ums config */
    let umsHost, umsAccountId: String
    let umsMsgRetainDay: Int
    
    private init?() {
        func loadString(section: Section?, key: String) -> String {
            guard let value = section?[key] else {
                NSLog("[Config] \(key) is not set")
                exit(1)
            }
            
            return value
        }
        
        do {
            guard let path = Bundle.main.path(forResource: "config", ofType: "ini") else { return nil }
            
            let config = try parseINI(filename: path)
            let rationalOwl = config["rational_owl"]
            let ums = config["ums"]
            
            /* rational owl fields */
            roGateHost = loadString(section: rationalOwl, key: "gate_host")
            self.roSvcId = loadString(section: rationalOwl, key: "svc_id")
            
            /* ums config */
            self.umsHost = loadString(section: ums, key: "ums_host")
            self.umsAccountId = loadString(section: ums, key: "account_id")
            umsMsgRetainDay = Int(loadString(section: ums, key: "msg_retain_day"))!
        } catch {
            NSLog("[\(type(of: self))] \(error)")
            return nil
        }
    }
}
