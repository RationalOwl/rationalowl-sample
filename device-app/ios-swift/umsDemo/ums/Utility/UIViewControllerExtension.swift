import UIKit

// Modification of https://stackoverflow.com/questions/31540375/how-to-create-a-toast-message-in-swift
extension UIViewController {
    func showToast(_ message: String) {
        let systemFont = UIFont.systemFont(ofSize: UIFont.labelFontSize)
        let size: CGSize = message.calculateSize(systemFont)

        let toastLabel = UILabel(frame: CGRect(x: (view.frame.size.width - size.width) / 2, y: view.frame.size.height - 100, width: size.width + 16, height: size.height + 16))
        toastLabel.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        toastLabel.textColor = UIColor.white
        // toastLabel.font = font
        toastLabel.textAlignment = .center
        toastLabel.text = message
        toastLabel.alpha = 1.0
        toastLabel.layer.cornerRadius = 10
        toastLabel.clipsToBounds = true
        view.addSubview(toastLabel)

        UIView.animate(withDuration: 4.0, delay: 0.1, options: .curveEaseOut, animations: {
            toastLabel.alpha = 0.0
        }, completion: { _ in
            toastLabel.removeFromSuperview()
        })
    }
}
