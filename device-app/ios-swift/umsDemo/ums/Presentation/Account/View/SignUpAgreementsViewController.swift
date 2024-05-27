import UIKit

class SignUpAgreementsViewController: UIViewController {
    private static let uncheckedImageName = "square"
    private static let checkedImageName = "checkmark.square.fill"

    @IBOutlet private var textViewTerms: UITextView!
    @IBOutlet private var checkBoxTerms: UIButton!
    @IBOutlet private var textViewPrivacyPolicy: UITextView!
    @IBOutlet private var checkBoxPrivacyPolicy: UIButton!
    @IBOutlet private var buttonNext: UIButton!

    var viewModel: SignUpViewModel!

    override func viewDidLoad() {
        super.viewDidLoad()

        loadTextAsset(fileName: "terms", textView: textViewTerms)
        loadTextAsset(fileName: "privacy_policy", textView: textViewPrivacyPolicy)

        viewModel.termsAgreement.observe(on: self) { [weak self] _ in
            self?.onCheckedChange()
        }

        viewModel.privacyPolicyAgreement.observe(on: self) { [weak self] _ in
            self?.onCheckedChange()
        }
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }

    private func loadTextAsset(fileName: String, textView: UITextView) {
        if let termsPath = Bundle.main.path(forResource: fileName, ofType: "txt") {
            do {
                textView.text = try String(contentsOfFile: termsPath, encoding: .utf8)
            } catch {}
        }
    }

    private func onCheckedChange() {
        buttonNext.isEnabled = viewModel.termsAgreement.value && viewModel.privacyPolicyAgreement.value
    }

    // MARK: Event handlers

    @IBAction private func onCheckBoxAgreeTermsClick(_: Any) {
        viewModel.termsAgreement.value = !viewModel.termsAgreement.value

        let imageName = viewModel.termsAgreement.value ? Self.checkedImageName : Self.uncheckedImageName
        checkBoxTerms.setImage(UIImage(systemName: imageName), for: .normal)
    }

    @IBAction private func onCheckBoxAgreePrivacyPolicyClick(_: Any) {
        viewModel.privacyPolicyAgreement.value = !viewModel.privacyPolicyAgreement.value

        let imageName = viewModel.privacyPolicyAgreement.value ? Self.checkedImageName : Self.uncheckedImageName
        checkBoxPrivacyPolicy.setImage(UIImage(systemName: imageName), for: .normal)
    }

    @IBAction private func onButtonNextClick(_: Any) {
        let viewController = storyboard?.instantiateViewController(withIdentifier: "SignUpInputInfoVC") as! SignUpInputInfoViewController
        viewController.viewModel = viewModel

        navigationController?.pushViewController(viewController, animated: true)
    }
}
