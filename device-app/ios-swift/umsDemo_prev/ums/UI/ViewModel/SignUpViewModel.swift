import Foundation
import RxCocoa
import RxSwift

class SignUpViewModel
{
    let repository = UserRepository.shared
    
    let phoneCountryCode = BehaviorRelay<String>(value: "82")
    let phoneNumber = BehaviorRelay<String>(value: "")
    let phoneAuthNumber = BehaviorRelay<String>(value: "")
    let termsAgreement = BehaviorRelay<Bool>(value: false)
    let privacyPolicyAgreement = BehaviorRelay<Bool>(value: false)
    let userName = BehaviorRelay<String>(value: "")
    let userId = BehaviorRelay<String>(value: "")
}
