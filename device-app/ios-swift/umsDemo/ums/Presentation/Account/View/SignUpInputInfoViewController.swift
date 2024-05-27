import UIKit

import RationalOwl

class SignUpInputInfoViewController: UIViewController {
    @IBOutlet private var textFieldName: UITextField!
    @IBOutlet private var textFieldId: UITextField!
    @IBOutlet private var buttonComplete: UIButton!

    var viewModel: SignUpViewModel!

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }

    private func registerDevice() {
        let minMgr = MinervaManager.getInstance()
        minMgr?.setDeviceRegisterResultDelegate(self)

        let gateHost: String = Config.shared.roGateHost
        let serviceId: String = Config.shared.roSvcId
        minMgr?.registerDevice(gateHost, serviceId: serviceId, deviceRegName: viewModel.userName.value)
    }

    private func saveUser(_ deviceRegId: String) {
        let userId: String? = !viewModel.userId.value.isEmpty ? viewModel.userId.value : nil
        let user = DataDef.User(regId: deviceRegId,
                                phoneCountryCode: viewModel.phoneCountryCode.value,
                                phoneNumber: viewModel.phoneNumber.value,
                                name: viewModel.userName.value,
                                userId: userId)

        UserLocalDataSource.shared.user = user
    }

    private func registerUser(_ deviceRegId: String) {
        let request = PushAppProto.PushAppInstallReq(
            mAccountId: Config.shared.umsAccountId,
            mDeviceType: UmsProtocol.APP_TYPE_IOS,
            mDeviceRegId: deviceRegId,
            mPhoneNumber: viewModel.phoneNumber.value,
            mAppUserId: viewModel.userId.value,
            mUserName: viewModel.userName.value
        )

        UmsService.shared.signUp(request) { [weak self] result in
            guard let self = self else { return }

            switch result {
            case let .success(response):
                guard response.mResultCode == UmsResult.RESULT_OK || response.mResultCode == UmsResult.RESULT_PUSH_APP_USER_ALREADY_REGISTER else {
                    NSLog("[\(type(of: self))] [\(response.mResultCode)] \(String(describing: response.mComment))")

                    let message = "error_code_message".localize(response.mResultCode)
                    onSignUpError(message)
                    return
                }

                ServerLocalDataSource.shared.server = DataDef.Server(regId: response.mUmsServerRegId)

                let titleId, messageId: String

                if response.mResultCode == UmsResult.RESULT_OK {
                    titleId = "new_user"
                    messageId = "welcome_new_user"
                } else {
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

            case let .failure(error):
                NSLog("[\(type(of: self))] \(error)")

                onSignUpError(error.errorDescription!)
                return
            }
        }
    }

    // MARK: Private methods
    private func onSignUpError(_ message: String) {
        let alert = UIAlertController(title: "sign_up_error".localize(),
                                      message: message,
                                      preferredStyle: .alert)

        alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
        present(alert, animated: true)
    }

    // MARK: Event handlers

    @IBAction private func onTextFieldNameChange(_ sender: UITextField) {
        viewModel.userName.value = sender.text!
        buttonComplete.isEnabled = !sender.text!.isEmpty
    }

    @IBAction private func onTextFieldIdChange(_ sender: UITextField) {
        viewModel.userId.value = sender.text!
    }

    @IBAction private func onButtonCompleteClick(_: UIButton) {
        registerDevice()
    }
}

extension SignUpInputInfoViewController: DeviceRegisterResultDelegate {
    func onRegisterResult(_ resultCode: Int32, resultMsg: String!, deviceRegId: String!) {
        NSLog("[\(type(of: self))] DeviceRegisterResultDelegate.onRegisterResult(resultCode: \(resultCode), resultMsg: \(String(describing: resultMsg)), deviceRegId: \(String(describing: deviceRegId))")

        if resultCode != RESULT_OK && resultCode != RESULT_DEVICE_ALREADY_REGISTERED {
            NSLog("[\(type(of: self))] DeviceRegisterResultDelegate.onRegisterResult failed: [\(resultCode)] \(String(describing: resultMsg))")

            let alert = UIAlertController(title: "register_device_error".localize(),
                                          message: "[\(resultCode)] \(resultMsg!)",
                                          preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
            present(alert, animated: true)

            return
        }

        saveUser(deviceRegId)
        registerUser(deviceRegId)
    }

    func onUnregisterResult(_: Int32, resultMsg _: String!) {}
}
