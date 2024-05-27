import Combine
import Foundation

class SignUpViewModel {
    let repository = UserLocalDataSource.shared

    let phoneCountryCode: Observable<String> = Observable("82")
    let phoneNumber: Observable<String> = Observable("")
    let phoneAuthNumber: Observable<String> = Observable("")
    let termsAgreement: Observable<Bool> = Observable(false)
    let privacyPolicyAgreement: Observable<Bool> = Observable(false)
    let userName: Observable<String> = Observable("")
    let userId: Observable<String> = Observable("")
}
