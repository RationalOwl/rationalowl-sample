import Foundation

class JsonRepository<T: Codable>
{
    private let encoder: JSONEncoder
    private let decoder: JSONDecoder
    
    let path: URL
    
    init(_ fileName: String)
    {
        let directory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
        path = directory.appendingPathComponent(fileName)
        
        encoder = JSONEncoder()
        encoder.dateEncodingStrategy = .millisecondsSince1970
        
        decoder = JSONDecoder()
        decoder.dateDecodingStrategy = .millisecondsSince1970
    }
    
    func getValue() -> T?
    {
        do
        {
            let data = try Data(contentsOf: path)
            return try decoder.decode(T.self, from: data)
        } catch {
            print(error)
        }
        
        return nil;
    }
    
    func setValue(_ value: T)
    {
        do
        {
            let data = try encoder.encode(value)
            try data.write(to: path, options: .atomic)
        } catch {
            print(error)
        }
    }
    
    func delete() -> Bool
    {
        do {
            try FileManager.default.removeItem(at: path)
            return true
        } catch {
            print(error)
            return false
        }
    }
}
