import Foundation
import UIKit

import RationalOwl

class UserInfoViewController: UIViewController {
    @IBOutlet private var labelUserName: UILabel!
    @IBOutlet private var labelUserId: UILabel!
    @IBOutlet private var labelPhoneNumber: UILabel!
    @IBOutlet private var labelJoinedDate: UILabel!
    
    var user: DataDef.User!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.isToolbarHidden = false
        
        labelUserName.text = user.name
        labelUserId.text = user.userId ?? "미사용"
        labelPhoneNumber.text = user.phoneNumber
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "joined_date_format".localize()
        labelJoinedDate.text = dateFormatter.string(from: user.joinedAt)
    }
    
    // MARK: Event handlers
    
    @IBAction private func onButtonDeleteAccountClick(_: Any) {
        let alert = UIAlertController(title: "delete_account".localize(),
                                      message: "delete_account_message".localize(),
                                      preferredStyle: .alert)
        
        alert.addAction(UIAlertAction(title: "delete_account".localize(), style: .destructive) { [weak self] _ in
            guard let self = self else { return }
            
            let user = UserLocalDataSource.shared.user!
            let request = PushAppProto.PushAppUnregUserReq(
                mAccountId: Config.shared.umsAccountId,
                mDeviceRegId: user.regId
            )
            
            UmsService.shared.deleteUser(request) { [weak self] result in
                guard let self = self else { return }
                
                switch result {
                case let .success(response):
                    guard response.mResultCode == UmsResult.RESULT_OK else {
                        let message = "error_code_message".localize(response.mResultCode)
                        onError(message)
                        return
                    }
                case let .failure(error):
                    NSLog("[\(type(of: self))] \(error)")
                    onError(error.errorDescription!)
                    return
                }
                
                _ = MessageLocalDataSource.shared.delete()
                _ = ServerLocalDataSource.shared.delete()
                _ = UserLocalDataSource.shared.delete()
                
                let serviceId: String = Config.shared.roSvcId
                let minMgr = MinervaManager.getInstance()
                minMgr?.unregisterDevice(serviceId)
                
                let storyboard = UIStoryboard(name: "SignUp", bundle: nil)
                let viewController = storyboard.instantiateInitialViewController()!
                
                UIApplication.shared.windows.first?.rootViewController = viewController
                UIApplication.shared.windows.first?.makeKeyAndVisible()
            }
        })
        alert.addAction(UIAlertAction(title: "cancel".localize(), style: .default))
        
        present(alert, animated: true)
    }
    
    private func onError(_ message: String) {
        let alert = UIAlertController(title: "delete_account_error".localize(),
                                      message: message,
                                      preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
        self.present(alert, animated: true)
    }
}
