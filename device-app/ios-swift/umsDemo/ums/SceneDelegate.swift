import RationalOwl
import UIKit

class SceneDelegate: UIResponder, UIWindowSceneDelegate {
    private static let appGroupId = "group.com.rationalowl.umsdemo"
    var window: UIWindow?

    private let userDefaults = UserDefaults(suiteName: appGroupId)!

    func scene(_ scene: UIScene, willConnectTo _: UISceneSession, options _: UIScene.ConnectionOptions) {
        guard let windowScene = (scene as? UIWindowScene) else { return }
        let isSignedUp = UserLocalDataSource.shared.user != nil

        let storyboard = UIStoryboard(name: isSignedUp ? "Message" : "SignUp", bundle: nil)
        let viewController = storyboard.instantiateInitialViewController()

        window = UIWindow(windowScene: windowScene)
        window!.rootViewController = viewController
        window!.makeKeyAndVisible()
    }

    func sceneWillResignActive(_: UIScene) {
        NSLog("[\(type(of: self))] sceneWillResignActive")

        let minMgr = MinervaManager.getInstance()
        minMgr?.enterBackground()

        userDefaults.setValue(false, forKey: "isActive")
    }

    func sceneDidBecomeActive(_: UIScene) {
        NSLog("[\(type(of: self))] sceneDidBecomeActive")

        let minMgr = MinervaManager.getInstance()
        minMgr?.becomeActive()

        userDefaults.setValue(true, forKey: "isActive")
        MessageSyncService.shared.syncMessages()
    }
}
