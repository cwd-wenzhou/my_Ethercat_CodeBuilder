/*
 * @Author: cwd-wenzhou 619715109@qq.com
 * @Date: 2022-11-22 13:58:53
 * @LastEditors: cwd-wenzhou 619715109@qq.com
 * @LastEditTime: 2022-11-25 13:17:21
 * @FilePath: /Ethercat-server-control/slaves/EL3064.h
 * @Description:
 *
 * Copyright (c) 2022 by cwd-wenzhou 619715109@qq.com, All Rights Reserved.
 */
#ifndef ETHERCAT_SERVER_CONTROL_EL3064_H
#define ETHERCAT_SERVER_CONTROL_EL3064_H

#include "../../include/slave.h"

struct EL3064_PDO_OFFSET {
    unsigned int offset_6000[4];
    unsigned int offset_6010[4];
    unsigned int offset_6020[4];
    unsigned int offset_6030[4];
};

//该从站的TXpdo
class EL3064_TxPDO {
public:
    bool status_underrange[4];
    bool status_overrange[4];
    bool status_limit_1_1[4];
    bool status_limit_1_2[4];

    bool status_limit_2_1[4];
    bool status_limit_2_2[4];

    bool status_error[4];
    bool status_Txpdo_state[4];
    bool status_Txpdo_Toggle[4];
    int value[4];
};

class EL3064 : public SLAVE {
public:
    EL3064_TxPDO *el3064TxPdo;
    struct EL3064_PDO_OFFSET pdo_offset; //从站变量位移
    void config(int position) override;

    void read_data() override;

    void send_data() override;

    void process_data() override;

    void print() override;

    ec_pdo_entry_reg_t *Domain_regs(uint16_t position, uint32_t vendor_id, uint32_t product_code) override;

    ec_sync_info_t *get_ec_sync_info_t_() override;

    ~EL3064() override;
};

extern ec_pdo_entry_info_t EL3064_pdo_entries[40];
extern ec_pdo_info_t EL3064_pdos[4];
extern ec_sync_info_t EL3064_syncs[5];

#endif // ETHERCAT_SERVER_CONTROL_EL3064_H
