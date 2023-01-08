#import "HardwareButtonsPlugin.h"
#if __has_include(<hardware_buttons/hardware_buttons-Swift.h>)
#import <hardware_buttons/hardware_buttons-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "hardware_buttons-Swift.h"
#endif

@implementation HardwareButtonsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftHardwareButtonsPlugin registerWithRegistrar:registrar];
}
@end
