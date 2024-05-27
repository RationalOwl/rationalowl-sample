import UIKit

class MessageReadViewController: UIViewController {
    private static var imageCache: [String: Data] = [:]

    @IBOutlet private var imageViewIcon: UIImageView!
    @IBOutlet private var labelTitle: UILabel!
    @IBOutlet private var textViewBody: UITextView!
    @IBOutlet private var imageView: UIImageView!

    var viewModel: MessageReadViewModel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.isToolbarHidden = false

        viewModel.image.observe(on: self) { [weak self] imageData in
            guard imageData != nil else { return }
            self?.loadImage(imageData!)
        }
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "message_long_datetime_format".localize()
        navigationItem.title = dateFormatter.string(from: viewModel.message.receivedAt)

        if viewModel.message.type == .emergency {
            imageViewIcon.image = UIImage(named: "MessageOrangeImage")
            labelTitle.text = "message_emergency_title".localize(viewModel.message.title)
        } else {
            imageViewIcon.image = UIImage(named: "MessageGreenImage")
            labelTitle.text = "message_normal_title".localize(viewModel.message.title)
        }

        textViewBody.text = viewModel.message.body
    }

    private func loadImage(_ data: Data) {
        let image = UIImage(data: data)!
        imageView.image = image
        imageView.isHidden = false
        imageView.widthAnchor.constraint(equalTo: imageView.heightAnchor, multiplier: image.size.width / image.size.height).isActive = true
    }
    
    // MARK: Event handlers
    @IBAction func onButtonItemCopyClick(_: Any) {
        UIPasteboard.general.string = viewModel.message.body
        showToast("copied_to_clipboard".localize())
    }

    @IBAction func onButtonItemDeleteClick(_: Any) {
        let alert = UIAlertController(title: nil, message: "delete_message".localize(), preferredStyle: .alert)

        alert.addAction(UIAlertAction(title: "ok".localize(), style: .destructive) { [weak self] _ in
            guard let self = self else { return }

            MessageLocalDataSource.shared.removeMessage(self.viewModel.message.id)
            self.navigationController?.popViewController(animated: true)
        })
        alert.addAction(UIAlertAction(title: "cancel".localize(), style: .cancel))

        present(alert, animated: true)
    }

    @IBAction func onButtonItemInfoClick(_: Any) {
        MessageRepository.shared.getDeliveryInfo(viewModel.message) { [weak self] result in
            let alert: UIAlertController
            
            switch result {
            case .success(let message):
                alert = UIAlertController(title: "message_delivery_info".localize(), message: nil, preferredStyle: .alert)
                
                let viewController = MessageInfoViewController()
                viewController.message = message
                
                alert.setValue(viewController, forKey: "contentViewController")
                alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
            case .failure(let error):
                alert = UIAlertController(title: "message_delivery_info".localize(),
                                          message: error.localizedDescription,
                                          preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
            }
            
            self?.present(alert, animated: true)
        }
    }
}
