import Foundation

class JsonLocalDataSource<T: Codable> {
    private let encoder: JSONEncoder
    private let decoder: JSONDecoder

    let file: URL

    init(_ fileName: String) {
        let directory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
        file = directory.appendingPathComponent(fileName)

        encoder = JSONEncoder()
        encoder.dateEncodingStrategy = .millisecondsSince1970

        decoder = JSONDecoder()
        decoder.dateDecodingStrategy = .millisecondsSince1970
    }

    var value: T? {
        get {
            do {
                if !FileManager.default.fileExists(atPath: file.path) {
                    return nil
                }

                let data = try Data(contentsOf: file)
                return try decoder.decode(T.self, from: data)
            } catch {
                NSLog("[\(type(of: self))] An error occurred while reading \(file.path): \(error)")
            }

            return nil
        }
        set {
            do {
                let data = try encoder.encode(newValue)
                try data.write(to: file, options: .atomic)
            } catch {
                NSLog("[\(type(of: self))] An error occurred while writing \(file.path): \(error)")
            }
        }
    }

    func delete() -> Bool {
        do {
            try FileManager.default.removeItem(at: file)
            return true
        } catch {
            NSLog("[\(type(of: self))] An error occurred while deleting \(file.path): \(error)")
            return false
        }
    }
}
