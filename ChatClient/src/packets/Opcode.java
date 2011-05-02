package packets;

public interface Opcode
{
    final byte CMSG_LOGIN                 = 0x01;
    final byte CMSG_LOGOUT                = 0x02;
    
    final byte SMSG_LOGIN_SUCCESS         = 0x03;
    final byte SMSG_LOGIN_FAILED          = 0x04;
    final byte SMSG_MULTI_LOGIN           = 0x05;
    
    final byte CMSG_CREATEROOM            = 0x06;
    final byte CMSG_JOINROOM              = 0x07;
    final byte CMSG_LEAVEROOM             = 0x08;
    
    final byte SMSG_CREATEROOM_SUCCESS    = 0x09;
    final byte SMSG_MULTI_CREATEROOM      = 0x0A;
    final byte SMSG_JOINROOM_SUCCESS      = 0x0B;
    final byte SMSG_JOINROOM_FAILED       = 0x0C;
    final byte SMSG_LEAVEROOM_SUCCESS     = 0x0D;
    
    
    final byte SMSG_SEND_ROOMLIST         = 0x0E;
    final byte SMSG_END_SEND_ROOMLIST     = 0x0F;
    
    final byte MSG_SENDGROUPMESSAGE       = 0x10;
    
    final byte SMSG_SEND_QUESTION         = 0x12;
    final byte CMSG_SEND_ANSWER           = 0x13;
    final byte CMSG_CANCEL_ANSWER         = 0x14;
    
    final byte CMSG_REQUEST_ROOMCLIENT    = 0x15;
    final byte SMSG_SEND_ROOMCLIENT       = 0x16;
    
    final byte MSG_WHISPERMESSAGE         = 0X17;
    final byte MSG_WHISPERMESSAGE_FAILED  = 0X18;
}