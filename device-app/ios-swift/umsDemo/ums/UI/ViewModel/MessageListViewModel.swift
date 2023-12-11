import Foundation
import RxCocoa
import RxSwift

class MessageListViewModel
{
    let repository = MessagesRepository.shared
    
    let viewType = BehaviorRelay<MessageListViewType>(value: .normal)
    let items: BehaviorRelay<[Message]>
    let selectedItemIds = BehaviorRelay<[String]>(value: [])
    
    init()
    {
        items = repository.messages
    }
    
    func setSelectedState(_ messageId: String, selected: Bool)
    {
        NSLog("[\(type(of: self))] setSelectedState(\(messageId), \(selected))")
        var newCheckedItems = selectedItemIds.value
        
        if selected
        {
            if newCheckedItems.contains(messageId) { return }
            newCheckedItems.append(messageId)
        }
        else
        {
            if let index = newCheckedItems.firstIndex(of: messageId)
            {
                newCheckedItems.remove(at: index)
            }
        }
        
        selectedItemIds.accept(newCheckedItems)
    }
    
    func removeSelectedItems()
    {
        NSLog("[\(type(of: self))] removeSelectedItems()")
        
        repository.removeMessages(selectedItemIds.value)
        selectedItemIds.accept([])
    }
    
    func notifyItemsChange()
    {
        items.accept(items.value)
    }
}
