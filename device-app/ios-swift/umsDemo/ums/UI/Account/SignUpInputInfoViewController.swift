import UIKit
import RxSwift
import RationalOwl

class SignUpInputInfoViewController: UIViewController, DeviceRegisterResultDelegate
{
    @IBOutlet var textFieldName: UITextField!
    @IBOutlet var textFieldId: UITextField!
    @IBOutlet var buttonComplete: UIButton!
    
    var viewModel: SignUpViewModel!
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        textFieldName.rx.text.orEmpty.bind(to: viewModel.userName).disposed(by: disposeBag)
        textFieldName.rx.text.bind(onNext: { [weak self] text in
            self?.buttonComplete.isEnabled = !text!.isEmpty
        }).disposed(by: disposeBag)
        
        textFieldId.rx.text.orEmpty.bind(to: viewModel.userId).disposed(by: disposeBag)
    }
    
    override func viewWillAppear(_ animated: Bool)
    {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }
    
    @IBAction func onButtonCompleteClick(_ sender: UIButton)
    {
        registerDevice()
    }
    
    private func registerDevice()
    {
        let manager: MinervaManager = MinervaManager.getInstance()
        manager.setDeviceRegisterResultDelegate(self)
        
        let gateHost: String = Config.shared.roGateHost
        let serviceId: String = Config.shared.roSvcId
        manager.registerDevice(gateHost, serviceId: serviceId, deviceRegName: viewModel.userName.value)
    }
    
    func onRegisterResult(_ resultCode: Int32, resultMsg: String!, deviceRegId: String!)
    {
        NSLog("[\(type(of: self))] onRegisterResult(resultCode: \(resultCode), deviceRegId: \(deviceRegId))")
        
        if (resultCode != RESULT_OK && resultCode != RESULT_DEVICE_ALREADY_REGISTERED)
        {
            NSLog("[\(type(of: self))] registerDevice error: \(resultMsg)");
            
            let alert = UIAlertController(title: "register_device_error".localize(),
                                          message: "[\(resultCode)] \(resultMsg)",
                                          preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
            self.present(alert, animated: true)
            
            return
        }
        
        saveUser(deviceRegId)
        registerUser(deviceRegId)
    }
    
    func onUnregisterResult(_ resultCode: Int32, resultMsg: String!) {}
    
    private func saveUser(_ deviceRegId: String)
    {
        let userId: String? = !viewModel.userId.value.isEmpty ? viewModel.userId.value : nil
        let user = User(regId: deviceRegId,
                        phoneCountryCode: viewModel.phoneCountryCode.value,
                        phoneNumber: viewModel.phoneNumber.value,
                        name: viewModel.userName.value,
                        userId: userId)
        
        UserRepository.shared.user = user
    }
    
    private func registerUser(_ deviceRegId: String)
    {
        let request = SignUpRequest(deviceRegId: deviceRegId,
                                    phoneNumber: viewModel.phoneNumber.value,
                                    userId: viewModel.userId.value,
                                    userName: viewModel.userName.value)
        
        UmsService.shared.signUp(request) { [weak self] result in
            guard let self = self else { return }
            
            switch result
            {
            case .success(let response):
                guard response.resultCode == .ok || response.resultCode == .userAlreadyRegistered else
                {
                    NSLog("[\(type(of: self))] Result code: \(response.resultCode)")
                    
                    let alert = UIAlertController(title: "sign_up_error".localize(),
                                                  message: "error_code_message".localize(response.resultCode.rawValue),
                                                  preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
                    self.present(alert, animated: true)
                    
                    return
                }
                
                ServerRepository.shared.server = Server(regId: response.umsServerRegId!)
                
                let titleId, messageId: String
                
                if response.resultCode == ResultCode.ok
                {
                    titleId = "new_user"
                    messageId = "welcome_new_user"
                }
                else
                {
                    titleId = "old_user"
                    messageId = "welcome_old_user"
                }
                
                let alert = UIAlertController(title: titleId.localize(),
                                              message: messageId.localize(self.viewModel.userName.value),
                                              preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "ok".localize(), style: .default) { _ in
                    let storyboard = UIStoryboard(name: "Message", bundle: nil)
                    let viewController = storyboard.instantiateInitialViewController()!
                    
                    UIApplication.shared.windows.first?.rootViewController = viewController
                    UIApplication.shared.windows.first?.makeKeyAndVisible()
                })
                self.present(alert, animated: true)
            case .failure(let error):
                NSLog("[\(type(of: self))] Error: \(error)")
                
                let alert = UIAlertController(title: "sign_up_error".localize(),
                                              message: error.errorDescription,
                                              preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
                self.present(alert, animated: true)
                
                return
            }
        }
    }
}
