import UIKit
import RxSwift

class SignUpVerifyPhoneViewController: UIViewController, UITextFieldDelegate
{
    private let tag = "SignUpInputPhoneViewController"
    
    @IBOutlet private var labelInputVerificationCode: UILabel!
    @IBOutlet private var textFieldVerificationCode: UITextField!
    
    var viewModel: SignUpViewModel!
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        labelInputVerificationCode.text = "input_auth_number".localize(viewModel.phoneNumber.value)
        
        textFieldVerificationCode.delegate = self
        textFieldVerificationCode.rx.text.orEmpty.bind(to: viewModel.phoneAuthNumber).disposed(by: disposeBag)
    }
    
    override func viewWillAppear(_ animated: Bool)
    {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }
    
    @IBAction func onButtonNextClick(_ sender: Any)
    {
        let request = VerifyAuthNumberRequest(phoneNumber: viewModel.phoneNumber.value,
                                              authNumber: viewModel.phoneAuthNumber.value)
        
        UmsService.shared.verifyAuthNumber(request) { [weak self] result in
            guard let self = self else { return }
            
            switch result
            {
            case .success(let response):
                guard response.resultCode == .ok else
                {
                    let alert = UIAlertController(title: "auth_failed".localize(),
                                                  message: response.comment,
                                                  preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
                    self.present(alert, animated: true)
                    
                    return
                }
            case .failure(let error):
                print("[\(self.tag)] Error: \(error)")
                
                let alert = UIAlertController(title: "auth_error".localize(),
                                              message: error.errorDescription,
                                              preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
                self.present(alert, animated: true)
                
                return
            }
            
            let viewController = self.storyboard?.instantiateViewController(withIdentifier: "SignUpAgreementsVC") as! SignUpAgreementsViewController
            viewController.viewModel = self.viewModel
            
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }
    
    @IBAction func onButtonEditPhoneNumberClick(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    
    // https://stackoverflow.com/questions/30973044/how-to-restrict-uitextfield-to-take-only-numbers-in-swift
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool
    {
        let aSet = NSCharacterSet(charactersIn: "0123456789").inverted
        let compSepByCharInSet = string.components(separatedBy: aSet)
        let numberFiltered = compSepByCharInSet.joined(separator: "")
        return string == numberFiltered
    }
}
