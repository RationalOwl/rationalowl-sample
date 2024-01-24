import UIKit
import RationalOwl

class UserInfoViewController : UIViewController
{
    private let tag = "UserInfoViewController"
    
    @IBOutlet var labelUserName: UILabel!
    @IBOutlet var labelUserId: UILabel!
    @IBOutlet var labelPhoneNumber: UILabel!
    @IBOutlet var labelJoinedDate: UILabel!
    
    var user: User!
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.navigationController?.isToolbarHidden = false
        
        labelUserName.text = user.name
        labelUserId.text = user.userId ?? "미사용"
        labelPhoneNumber.text = user.phoneNumber
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "joined_date_format".localize()
        labelJoinedDate.text = dateFormatter.string(from: user.joinedAt)
    }
    
    @IBAction func onButtonDeleteAccountClick(_ sender: Any)
    {
        let alert = UIAlertController(title: "delete_account".localize(),
                                      message: "delete_account_message".localize(),
                                      preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "delete_account".localize(), style: .destructive) { [weak self] _ in
            guard let self = self else { return }
            
            let user = UserRepository.shared.user!
            let request = DeleteUserRequest(phoneNumber: user.phoneNumber)
            
            UmsService.shared.deleteUser(request) { [weak self] result in
                guard let self = self else { return }
                
                switch result
                {
                case .success(let response):
                    guard response.resultCode == .ok else
                    {
                        let alert = UIAlertController(title: "delete_account_error".localize(),
                                                      message: "error_code_message".localize(response.resultCode.rawValue),
                                                      preferredStyle: .alert)
                        alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
                        self.present(alert, animated: true)
                        
                        return
                    }
                case .failure(let error):
                    print("[\(self.tag)] Error: \(error)")
                    
                    let alert = UIAlertController(title: "delete_account_error".localize(),
                                                  message: error.errorDescription,
                                                  preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
                    self.present(alert, animated: true)
                    
                    return
                }
                
                _ = UserRepository.shared.delete()
                _ = MessagesRepository.shared.delete()
                
                let serviceId: String = Config.shared.roSvcId
                MinervaManager.getInstance().unregisterDevice(serviceId)
                
                exit(0)
            }
        })
        alert.addAction(UIAlertAction(title: "cancel".localize(), style: .default))
        self.present(alert, animated: true)
    }
}
