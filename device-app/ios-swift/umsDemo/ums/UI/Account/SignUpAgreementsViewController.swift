import UIKit
import RxCocoa
import RxSwift

class SignUpAgreementsViewController: UIViewController
{
    private let uncheckedImageName = "square"
    private let checkedImageName = "checkmark.square.fill"
    
    @IBOutlet private var textViewTerms: UITextView!
    @IBOutlet private var checkBoxTerms: UIButton!
    @IBOutlet private var textViewPrivacyPolicy: UITextView!
    @IBOutlet private var checkBoxPrivacyPolicy: UIButton!
    @IBOutlet private var buttonNext: UIButton!
    
    var viewModel: SignUpViewModel!
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        if let termsPath = Bundle.main.path(forResource: "terms", ofType: "txt")
        {
            do {
                textViewTerms.text = try String(contentsOfFile: termsPath, encoding: .utf8)
            } catch { }
        }
        
        if let privacyPolicyPath = Bundle.main.path(forResource: "privacy_policy", ofType: "txt")
        {
            do {
                textViewPrivacyPolicy.text = try String(contentsOfFile: privacyPolicyPath, encoding: .utf8)
            } catch { }
        }
        
        BehaviorRelay.merge(viewModel.termsAgreement.asObservable(), viewModel.privacyPolicyAgreement.asObservable()).bind { [weak self] _ in
            guard let self = self else { return }
            
            self.buttonNext.isEnabled = self.viewModel.termsAgreement.value && self.viewModel.privacyPolicyAgreement.value
        }.disposed(by: disposeBag)
    }
    
    override func viewWillAppear(_ animated: Bool)
    {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }
    
    @IBAction func onCheckBoxAgreeTermsClick(_ sender: Any)
    {
        viewModel.termsAgreement.accept(!viewModel.termsAgreement.value)
        let imageName = viewModel.termsAgreement.value ? checkedImageName : uncheckedImageName
        checkBoxTerms.setImage(UIImage(systemName: imageName), for: .normal)
    }
    
    @IBAction func onCheckBoxAgreePrivacyPolicyClick(_ sender: Any)
    {
        viewModel.privacyPolicyAgreement.accept(!viewModel.privacyPolicyAgreement.value)
        let imageName = viewModel.privacyPolicyAgreement.value ? checkedImageName : uncheckedImageName
        checkBoxPrivacyPolicy.setImage(UIImage(systemName: imageName), for: .normal)
    }
    
    @IBAction func onButtonNextClick(_ sender: Any)
    {
        let viewController = self.storyboard?.instantiateViewController(withIdentifier: "SignUpInputInfoVC") as! SignUpInputInfoViewController
        viewController.viewModel = viewModel
        
        self.navigationController?.pushViewController(viewController, animated: true)
    }
}
