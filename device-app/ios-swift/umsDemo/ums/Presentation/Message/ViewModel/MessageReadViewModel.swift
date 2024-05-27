import Foundation

class MessageReadViewModel {
    let message: DataDef.Message
    let image: Observable<Data?> = Observable(nil)
    
    init(_ message: DataDef.Message) {
        self.message = message
        
        if message.imageId != nil {
            fetchImage()
        }
    }
    
    private func fetchImage() {
        MessageRepository.shared.getImage(message) { [weak self] result in
            if case .success(let imageData) = result {
                self?.image.value = imageData
            }
        }
    }
}
