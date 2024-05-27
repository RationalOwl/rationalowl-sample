import Foundation

class MessageListViewModel {
    enum ViewType {
        case normal
        case delete
    }
    
    let viewType: Observable<ViewType> = Observable(.normal)
    let items: Observable<[DataDef.Message]> = Observable([])
    let selectedItemIds: Observable<Set<String>> = Observable([])

    init() {
        items.value = MessageLocalDataSource.shared.messages.value

        MessageLocalDataSource.shared.messages.observe(on: self) { [weak self] messages in
            self?.items.value = Array(messages)
        }
    }

    func setSelectedState(_ messageId: String, selected: Bool) {
        NSLog("[\(type(of: self))] setSelectedState(\(messageId), \(selected))")
        var newCheckedItems = selectedItemIds.value

        if selected {
            if newCheckedItems.contains(messageId) { return }
            newCheckedItems.insert(messageId)
        } else {
            if let index = newCheckedItems.firstIndex(of: messageId) {
                newCheckedItems.remove(at: index)
            }
        }

        selectedItemIds.value = newCheckedItems
    }

    func removeSelectedItems() {
        NSLog("[\(type(of: self))] removeSelectedItems()")

        MessageLocalDataSource.shared.removeMessages(selectedItemIds.value)
        selectedItemIds.value = []
    }

    func notifyItemsChange() {
        items.notifyObservers()
    }
}
