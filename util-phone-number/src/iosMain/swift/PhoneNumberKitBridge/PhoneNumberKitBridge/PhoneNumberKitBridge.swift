import Foundation
import PhoneNumberKit

@objc public class PhoneNumberKitBridge: NSObject {

    private let phoneNumberUtility = PhoneNumberUtility()

    @objc public func format(_ number: String) -> String {
        do {
            let parsedNumber = try phoneNumberUtility.parse(number)
            return phoneNumberUtility.format(parsedNumber, toType: .international)
        } catch {
            return number
        }
    }

    @objc public func isValid(_ number: String) -> Bool {
        return phoneNumberUtility.isValidPhoneNumber(number)
    }
}
