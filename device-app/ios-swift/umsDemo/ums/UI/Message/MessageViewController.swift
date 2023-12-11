import UIKit

class MessageViewController : UIViewController
{
    private var imageCache: [String: Data] = [:]
    
    @IBOutlet var imageViewIcon: UIImageView!
    @IBOutlet var labelTitle: UILabel!
    @IBOutlet var textViewBody: UITextView!
    @IBOutlet var imageView: UIImageView!
    
    var message: Message!
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        navigationController?.isToolbarHidden = false
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "message_long_datetime_format".localize()
        navigationItem.title = dateFormatter.string(from: message.receivedAt)
        
        if message.type == .emergency
        {
            imageViewIcon.image = UIImage(named: "MessageOrangeImage")
            labelTitle.text = "message_emergency_title".localize(message.title)
        }
        else
        {
            imageViewIcon.image = UIImage(named: "MessageGreenImage")
            labelTitle.text = "message_normal_title".localize(message.title)
        }
        
        textViewBody.text = message.body
        
        if (message.imageId != nil)
        {
            loadImage()
        }
    }
    
    private func loadImage()
    {
        guard let imageId = self.message.imageId else { return }
        
        if let cache = imageCache[imageId]
        {
            showImage(cache)
            return
        }
        
        let request = ImageDataRequest(
            imageId: imageId,
            messageId: message.id
        )
        
        UmsService.shared.getImageData(request) { [weak self] result in
            guard let self = self else { return }
            
            switch result
            {
            case .success(let response):
                guard response.resultCode == .ok else
                {
                    NSLog("[\(type(of: self))] Result code: \(response.resultCode)")
                    return
                }
                
                let data = response.imageData!
                self.imageCache[imageId] = data
                self.showImage(data)
            case .failure(let error):
                NSLog("[\(type(of: self))] Error: \(error)")
                return
            }
        }
    }
    
    private func showImage(_ data: Data) {
        let image = UIImage(data: data)!
        imageView.image = image
        imageView.isHidden = false
        imageView.widthAnchor.constraint(equalTo: imageView.heightAnchor, multiplier: image.size.width / image.size.height).isActive = true
    }
    
    private func updateInfo(callback: @escaping () -> Void) {
        let diffDays: Int = Calendar.current.dateComponents([.day],
                                                            from: message.sentAt,
                                                            to: Date()).day ?? 0
        
        if diffDays >= 7
        {
            callback()
            return
        }
        
        let user = UserRepository.shared.user!
        let request = MessageInfoRequest(phoneNumber: user.phoneNumber,
                                         deviceRegId: user.regId,
                                         messageId: message.id)
        
        UmsService.shared.getMessageInfo(request) { [weak self] result in
            guard let self = self else { return }
            
            switch result {
            case .success(let response):
                guard response.resultCode == .ok else
                {
                    NSLog("[\(type(of: self))] Result code: \(response.resultCode)")
                    return
                }
                
                if response.alimTalkSentAt > 0
                {
                    self.message.alimTalkSentAt = Date(timeIntervalSince1970: TimeInterval(response.alimTalkSentAt) / 1000)
                }
                
                if response.munjaSentAt > 0
                {
                    self.message.munjaSentAt = Date(timeIntervalSince1970: TimeInterval(response.munjaSentAt) / 1000)
                }
                
                self.message.munjaType = response.munjaType
                
                MessagesRepository.shared.save()
                callback()
            case .failure(let error):
                NSLog("[\(type(of: self))] Error: \(error)")
                
                MessagesRepository.shared.save()
                callback()
            }
        }
    }
    
    @IBAction func onButtonItemCopyClick(_ sender: Any)
    {
        UIPasteboard.general.string = message.body
        showToast("copied_to_clipboard".localize())
    }
    
    @IBAction func onButtonItemDeleteClick(_ sender: Any)
    {
        let alert = UIAlertController(title: nil, message: "delete_message".localize(), preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "ok".localize(), style: .destructive) { [weak self] _ in
            guard let self = self else { return }
            
            MessagesRepository.shared.removeMessage(self.message.id)
            self.navigationController?.popViewController(animated: true)
        })
        alert.addAction(UIAlertAction(title: "cancel".localize(), style: .cancel))
        self.present(alert, animated: true)
    }
    
    @IBAction func onButtonItemInfoClick(_ sender: Any)
    {
        updateInfo {
            let alert = UIAlertController(title: "message_delivery_info".localize(), message: nil, preferredStyle: .alert)
            
            let viewController = MessageInfoViewController()
            viewController.message = self.message
            
            alert.setValue(viewController, forKey: "contentViewController")
            alert.addAction(UIAlertAction(title: "ok".localize(), style: .default))
            
            self.present(alert, animated: true)
        }
    }
}
