import UIKit

class SignUpInputPhoneViewController: UIViewController {
    @IBOutlet private var textFieldCountryCode: UITextField!
    @IBOutlet private var textFieldPhoneNumber: UITextField!
    @IBOutlet private var buttonNext: UIButton!

    let viewModel = SignUpViewModel()

    override func viewDidLoad() {
        super.viewDidLoad()

        let textViewCountryName = UITextView()
        textViewCountryName.text = "country_korea".localize()

        textFieldCountryCode.leftView = textViewCountryName
        textFieldCountryCode.leftViewMode = .always

        textFieldPhoneNumber.delegate = self
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }

    private func sendVerificationCode() {
        let request = PushAppProto.PushAppAuthNumberReq(
            mAccountId: Config.shared.umsAccountId,
            mCountryCode: viewModel.phoneCountryCode.value,
            mPhoneNumber: viewModel.phoneNumber.value
        )

        UmsService.shared.requestAuthNumber(request) { [weak self] result in
            guard let self = self else { return }

            switch result {
            case let .success(response):
                guard response.mResultCode == UmsResult.RESULT_OK else {
                    NSLog("[\(type(of: self))] [\(response.mResultCode)] \(String(describing: response.mComment))")

                    let message = "error_code_message".localize(response.mResultCode)
                    onAuthError(message)
                    return
                }
            case let .failure(error):
                NSLog("[\(type(of: self))] Error: \(error)")

                onAuthError(error.errorDescription!)
                return
            }

            let viewController = self.storyboard?.instantiateViewController(withIdentifier: "SignUpVerifyPhoneVC") as! SignUpVerifyPhoneViewController
            viewController.viewModel = self.viewModel

            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }

    private func onAuthError(_ message: String) {
        let alert = UIAlertController(title: "phone_auth_error".localize(),
                                      message: message,
                                      preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
        present(alert, animated: true)
    }

    // MARK: Event handlers

    @IBAction private func onTextFieldPhoneChange(_ sender: UITextField) {
        viewModel.phoneNumber.value = sender.text!
        buttonNext.isEnabled = !sender.text!.isEmpty
    }

    @IBAction private func onButtonNextClick(_: UIButton) {
        let alert = UIAlertController(title: "phone_auth".localize(),
                                      message: "send_auth_number".localize(textFieldPhoneNumber.text!),
                                      preferredStyle: .alert)

        alert.addAction(UIAlertAction(title: "ok".localize(), style: .default) { [weak self] _ in
            self?.sendVerificationCode()
        })
        alert.addAction(UIAlertAction(title: "cancel".localize(), style: .cancel))

        present(alert, animated: true)
    }
}

extension SignUpInputPhoneViewController: UITextFieldDelegate {
    // https://stackoverflow.com/questions/30973044/how-to-restrict-uitextfield-to-take-only-numbers-in-swift?page=1&tab=scoredesc
    func textField(_: UITextField, shouldChangeCharactersIn _: NSRange, replacementString string: String) -> Bool {
        let aSet = NSCharacterSet(charactersIn: "0123456789").inverted
        let compSepByCharInSet = string.components(separatedBy: aSet)
        let numberFiltered = compSepByCharInSet.joined(separator: "")
        return string == numberFiltered
    }
}
