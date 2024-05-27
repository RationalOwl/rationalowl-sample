import UIKit

class MessageNavigationController: UINavigationController {
    var message: DataDef.Message?

    override func viewDidLoad() {
        super.viewDidLoad()

        let listViewController = storyboard!.instantiateViewController(withIdentifier: "MessageListVC") as! MessageListViewController
        listViewController.message = message
        pushViewController(listViewController, animated: false)

        if message != nil {
            let viewController = storyboard!.instantiateViewController(withIdentifier: "MessageReadVC") as! MessageReadViewController
            viewController.viewModel = MessageReadViewModel(message!)
            pushViewController(viewController, animated: true)
        }
    }
}
