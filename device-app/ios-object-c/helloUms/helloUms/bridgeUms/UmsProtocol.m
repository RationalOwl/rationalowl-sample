//
//  UmsProtocol.m
//  helloUms
//
//  Created by Mac on 5/24/25.
//

#import "UmsProtocol.h"

@implementation UmsProtocol

// App install
const int APP_AUTH_NUMBER_CMD_ID = 301;
const int APP_VERITY_AUTH_NUMBER_CMD_ID = 302;
const int APP_INSTALL_CMD_ID = 303;

const int APP_UNREG_USER_CMD_ID = 305;

// App job
const int APP_MSG_READ_NOTI_CMD_ID = 311;
const int APP_MSG_INFO_CMD_ID = 312;
const int APP_IMG_DATA_CMD_ID = 313;

// Device type
const int APP_TYPE_ANDROID = 1;
const int APP_TYPE_IOS = 2;

// Push msg data fields
NSString * const APP_PUSH_MSG_ID_KEY = @"mId";
NSString * const APP_PUSH_TYPE_KEY = @"type";
NSString * const APP_PUSH_TITLE_KEY = @"title";
NSString * const APP_PUSH_BODY_KEY = @"body";
NSString * const APP_SEND_TIME_KEY = @"st";
NSString * const APP_IMG_ID_KEY = @"ii";

@end
