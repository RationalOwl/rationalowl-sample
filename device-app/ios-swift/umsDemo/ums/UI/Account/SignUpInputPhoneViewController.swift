import UIKit
import RxSwift

class SignUpInputPhoneViewController: UIViewController, UITextFieldDelegate
{
    @IBOutlet private var textFieldCountryCode: UITextField!
    @IBOutlet private var textFieldPhoneNumber: UITextField!
    @IBOutlet private var buttonNext: UIButton!
    
    let viewModel = SignUpViewModel()
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        let textViewCountryName = UITextView()
        textViewCountryName.text = "country_korea".localize()
        
        textFieldCountryCode.leftView = textViewCountryName
        textFieldCountryCode.leftViewMode = .always
        
        textFieldPhoneNumber.delegate = self
        textFieldPhoneNumber.rx.text.orEmpty.bind(to: viewModel.phoneNumber).disposed(by: disposeBag)
        textFieldPhoneNumber.rx.text.subscribe(on: MainScheduler.instance).bind { [weak self] text in
            self?.buttonNext.isEnabled = !text!.isEmpty
        }.disposed(by: disposeBag)
    }
    
    override func viewWillAppear(_ animated: Bool)
    {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }
    
    @IBAction func onButtonNextClick(_ sender: UIButton)
    {
        let alert = UIAlertController(title: "phone_auth".localize(),
                                      message: "send_auth_number".localize(textFieldPhoneNumber.text!),
                                      preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "ok".localize(), style: .default) { [weak self] _ in
            self?.sendVerificationCode()
        })
        alert.addAction(UIAlertAction(title: "cancel".localize(), style: .cancel))
        self.present(alert, animated: true)
    }
    
    // https://stackoverflow.com/questions/30973044/how-to-restrict-uitextfield-to-take-only-numbers-in-swift?page=1&tab=scoredesc
    internal func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool
    {
        let aSet = NSCharacterSet(charactersIn: "0123456789").inverted
        let compSepByCharInSet = string.components(separatedBy: aSet)
        let numberFiltered = compSepByCharInSet.joined(separator: "")
        return string == numberFiltered
    }
    
    private func sendVerificationCode()
    {
        let request = AuthNumberRequest(countryCode: viewModel.phoneCountryCode.value,
                                        phoneNumber: viewModel.phoneNumber.value)
        
        UmsService.shared.requestAuthNumber(request) { [weak self] result in
            guard let self = self else { return }
            
            switch result
            {
            case .success(let response):
                guard response.resultCode == .ok else
                {
                    NSLog("[\(type(of: self))] Result code: \(response.resultCode)")
                    
                    let alert = UIAlertController(title: "phone_auth_error".localize(),
                                                  message: "error_code_message".localize(response.resultCode.rawValue),
                                                  preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
                    self.present(alert, animated: true)
                    
                    return
                }
            case .failure(let error):
                NSLog("[\(type(of: self))] Error: \(error)")
                
                let alert = UIAlertController(title: "phone_auth_error".localize(),
                                              message: error.errorDescription,
                                              preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
                self.present(alert, animated: true)
                
                return
            }
            
            let viewController = self.storyboard?.instantiateViewController(withIdentifier: "SignUpVerifyPhoneVC") as! SignUpVerifyPhoneViewController
            viewController.viewModel = self.viewModel
            
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }
}
