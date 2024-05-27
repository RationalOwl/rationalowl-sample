import UIKit

class SignUpVerifyPhoneViewController: UIViewController {
    private let tag = "SignUpInputPhoneViewController"

    @IBOutlet private var labelInputVerificationCode: UILabel!
    @IBOutlet private var textFieldVerificationCode: UITextField!

    var viewModel: SignUpViewModel!

    override func viewDidLoad() {
        super.viewDidLoad()

        labelInputVerificationCode.text = "input_auth_number".localize(viewModel.phoneNumber.value)

        textFieldVerificationCode.delegate = self
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }

    // MARK: Event handlers

    @IBAction private func onTextFieldVerificationChange(_: UITextField) {
        viewModel.phoneAuthNumber.value = textFieldVerificationCode.text!
    }

    @IBAction private func onButtonEditPhoneNumberClick(_: Any) {
        navigationController?.popViewController(animated: true)
    }

    @IBAction private func onButtonNextClick(_: Any) {
        let request = PushAppProto.PushAppVerifyAuthNumberReq(
            mAccountId: Config.shared.umsAccountId,
            mDeviceType: UmsProtocol.APP_TYPE_IOS,
            mPhoneNumber: viewModel.phoneNumber.value,
            mAuthNumber: viewModel.phoneAuthNumber.value
        )

        UmsService.shared.verifyAuthNumber(request) { [weak self] result in
            guard let self = self else { return }

            switch result {
            case let .success(response):
                guard response.mResultCode == UmsResult.RESULT_OK else {
                    NSLog("[\(type(of: self))] [\(response.mResultCode)] \(String(describing: response.mComment))")
                    onAuthFail(response.mComment!)
                    return
                }
            case let .failure(error):
                print("[\(self.tag)] Error: \(error)")

                onAuthFail(error.errorDescription!, isError: true)
                return
            }

            let viewController = self.storyboard?.instantiateViewController(withIdentifier: "SignUpAgreementsVC") as! SignUpAgreementsViewController
            viewController.viewModel = self.viewModel

            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }

    private func onAuthFail(_ message: String, isError: Bool = false) {
        let alert = UIAlertController(title: (!isError ? "auth_failed" : "auth_error").localize(),
                                      message: message,
                                      preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
        present(alert, animated: true)
    }
}

extension SignUpVerifyPhoneViewController: UITextFieldDelegate {
    // https://stackoverflow.com/questions/30973044/how-to-restrict-uitextfield-to-take-only-numbers-in-swift
    func textField(_: UITextField, shouldChangeCharactersIn _: NSRange, replacementString string: String) -> Bool {
        let aSet = NSCharacterSet(charactersIn: "0123456789").inverted
        let compSepByCharInSet = string.components(separatedBy: aSet)
        let numberFiltered = compSepByCharInSet.joined(separator: "")
        return string == numberFiltered
    }
}
