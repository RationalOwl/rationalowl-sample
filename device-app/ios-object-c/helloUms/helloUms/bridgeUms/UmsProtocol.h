//
//  UmsProtocol.h
//  helloUms
//
//  Created by Mac on 5/24/25.
//

#import <Foundation/Foundation.h>

@interface UmsProtocol : NSObject

/////////////////////////////////////////
// Push App
/////////////////////////////////////////

extern const int APP_AUTH_NUMBER_CMD_ID;
extern const int APP_VERITY_AUTH_NUMBER_CMD_ID;
extern const int APP_INSTALL_CMD_ID;

extern const int APP_UNREG_USER_CMD_ID;

// App job
extern const int APP_MSG_READ_NOTI_CMD_ID;
extern const int APP_MSG_INFO_CMD_ID;
extern const int APP_IMG_DATA_CMD_ID;

// Device type
extern const int APP_TYPE_ANDROID;
extern const int APP_TYPE_IOS;

// Push msg data fields
extern NSString * const APP_PUSH_MSG_ID_KEY;
extern NSString * const APP_PUSH_TYPE_KEY;
extern NSString * const APP_PUSH_TITLE_KEY;
extern NSString * const APP_PUSH_BODY_KEY;
extern NSString * const APP_SEND_TIME_KEY;
extern NSString * const APP_IMG_ID_KEY;

@end
