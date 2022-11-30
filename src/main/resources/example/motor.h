//
// Created by imc on 22-11-27.
//

#ifndef ETHERCAT_SERVER_CONTROL_MOTOR_H
#define ETHERCAT_SERVER_CONTROL_MOTOR_H

#include "../../include/slave.h"

struct MOTOR_PDO_OFFSET {
    unsigned int offset_6040;
    unsigned int offset_6060;
    unsigned int offset_607a;
    unsigned int offset_60ff;
    unsigned int offset_6071;

    unsigned int offset_6041;
    unsigned int offset_6061;
    unsigned int offset_6064;
    unsigned int offset_606c;
    unsigned int offset_6077;

};

//该从站的TXpdo
class MOTOR_TxPDO {
public:
    //#0x1A00
    uint16_t status_word;          //驱动器状态字
    int8_t mode_of_Operation_Display; //驱动器当前运行模式
    int32_t current_torque;  //电机当前运行力矩
    int32_t current_Velocity;  //电机当前运行速度
    int32_t current_Position;  //电机当前位置
};

//该从站的TXpdo
class MOTOR_RxPDO {
public:
    //#0x1600
    uint16_t control_word;
    int8_t mode_of_Operation;         //电机运行模式的设定值,默认位置模式
    int32_t target_position;   //电机的目标位置
    int32_t target_velocity;   //电机的目标速度
    int32_t target_torque;   //电机的目标力矩
};


class MOTOR : public SLAVE {
public:
    MOTOR_TxPDO* motorTxPdo;
    MOTOR_RxPDO* motorRxPdo;
    struct MOTOR_PDO_OFFSET pdo_offset; //从站变量位移
    void config(int position) override;
    void read_data() override;
    void send_data() override;
    void process_data() override;
    void print() override;
    ec_pdo_entry_reg_t *Domain_regs(uint16_t position, uint32_t vendor_id, uint32_t product_code) override;
    ec_sync_info_t *get_ec_sync_info_t_() override;

    ~MOTOR() override;
};

extern ec_pdo_entry_info_t MOTOR_pdo_entries[40];
extern ec_pdo_info_t MOTOR_pdos[4];
extern ec_sync_info_t MOTOR_syncs[5];


#endif //ETHERCAT_SERVER_CONTROL_MOTOR_H
