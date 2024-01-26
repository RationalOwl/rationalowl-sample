import UIKit
import RationalOwl

class SceneDelegate: UIResponder, UIWindowSceneDelegate
{
    private static let appGroupId = "group.com.rationalowl.umsdemo"
    var window: UIWindow?
    
    private let userDefaults = UserDefaults(suiteName: appGroupId)!
    private let manager: MinervaManager = MinervaManager.getInstance()
    
    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions)
    {
        guard let windowScene = (scene as? UIWindowScene) else { return }
        let isSignedUp = UserRepository.shared.user != nil
        
        let storyboard = UIStoryboard(name: (isSignedUp ? "Message" : "SignUp"), bundle: nil)
        let viewController = storyboard.instantiateInitialViewController()
        
        window = UIWindow(windowScene: windowScene)
        window!.rootViewController = viewController
        window!.makeKeyAndVisible()
    }
    
    func sceneWillResignActive(_ scene: UIScene)
    {
        NSLog("[\(type(of: self))] sceneWillResignActive")
        manager.enterBackground()
        userDefaults.setValue(false, forKey: "isActive")
    }

    func sceneDidBecomeActive(_ scene: UIScene)
    {
        NSLog("[\(type(of: self))] sceneDidBecomeActive")
        manager.becomeActive()
        userDefaults.setValue(true, forKey: "isActive")
        
        MessageSyncService.shared.syncMessages()
    }
}
