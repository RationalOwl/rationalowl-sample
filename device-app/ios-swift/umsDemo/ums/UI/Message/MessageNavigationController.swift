import UIKit

class MessageNavigationController : UINavigationController
{
    var message: Message?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        let listViewController = self.storyboard!.instantiateViewController(withIdentifier: "MessageListVC") as! MessageListViewController
        listViewController.message = message
        self.pushViewController(listViewController, animated: false)
        
        if message != nil {
            let viewController = self.storyboard!.instantiateViewController(withIdentifier: "MessageViewVC") as! MessageViewController
            viewController.message = message
            self.pushViewController(viewController, animated: true)
        }
    }
}
