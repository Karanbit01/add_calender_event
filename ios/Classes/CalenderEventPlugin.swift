import Flutter
import UIKit
import EventKit
import EventKitUI
import Foundation

public class CalenderEventPlugin: NSObject, FlutterPlugin {

  public static func register(with registrar: FlutterPluginRegistrar) {
          
    let channel = FlutterMethodChannel(name: "add_calender_method", binaryMessenger: registrar.messenger())
    let instance = CalenderEventPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        if call.method == "addCalanderEvent" {
        if let args = call.arguments as? Dictionary<String, Any>,
        let eventName = args["event_name"] as? String {
        let eventStore : EKEventStore = EKEventStore()
    var controller = UIApplication.shared.keyWindow?.rootViewController as! FlutterViewController

        eventStore.requestAccess(to: .event) { (granted, error) in
          
          if (granted) && (error == nil) {

              let event = EKEvent(eventStore: eventStore)
              event.title = eventName
              event.startDate = Date()
              event.endDate = Date()
              DispatchQueue.main.async {
                  let eventController = EKEventEditViewController()
                  eventController.event = event
                  eventController.eventStore = eventStore
                  eventController.editViewDelegate = controller
                  controller.present(eventController, animated: true, completion: nil)
                    result("Saved Event")

              }
          }
            else{
                result( "Failed to save event with error : \(error) or access not granted")
            }

            }
        }
      }

    // result("iOS " + UIDevice.current.systemVersion)
  }
}


extension FlutterViewController : EKEventEditViewDelegate, UINavigationControllerDelegate {
    
    // conform to the EKEventEditViewDelegate protocol
    public func eventEditViewController(_ controller: EKEventEditViewController, didCompleteWith action: EKEventEditViewAction) {
        dismiss(animated: true, completion: nil)
    }
    
    
}
