//
// Created by 61971 on 2022/11/24.
//
/*
 * @Author: cwd-wenzhou 619715109@qq.com
 * @Date: 2022-11-22 13:58:37
 * @LastEditors: cwd-wenzhou 619715109@qq.com
 * @LastEditTime: 2022-11-25 13:20:31
 * @FilePath: /Ethercat-server-control/slaves/EL3064.cpp
 * @Description:
 *
 * Copyright (c) 2022 by cwd-wenzhou 619715109@qq.com, All Rights Reserved.
 */
#include "include/EL3064.h"
//填充相关PDOS信息

ec_pdo_entry_info_t EL3064_pdo_entries[] = {
        // 0x1a00
        {0x6000, 1,  1},
        {0x6000, 2,  1},
        {0x6000, 3,  2},
        {0x6000, 5,  2},
        {0x6000, 7,  1},
        {0x0000, 0,  1}, // gap
        {0x0000, 0,  6}, // gap
        {0x6000, 15, 1},
        {0x6000, 16, 1},
        {0x6000, 17, 16},
        // 0x1a02
        {0x6010, 1,  1},
        {0x6010, 2,  1},
        {0x6010, 3,  2},
        {0x6010, 5,  2},
        {0x6010, 7,  1},
        {0x0000, 0,  1}, // gap
        {0x0000, 0,  6}, // gap
        {0x6010, 15, 1},
        {0x6010, 16, 1},
        {0x6010, 17, 16},
        // 0x1a04
        {0x6020, 1,  1},
        {0x6020, 2,  1},
        {0x6020, 3,  2},
        {0x6020, 5,  2},
        {0x6020, 7,  1},
        {0x0000, 0,  1}, // gap
        {0x0000, 0,  6}, // gap
        {0x6020, 15, 1},
        {0x6020, 16, 1},
        {0x6020, 17, 16},
        // 0x1a06
        {0x6030, 1,  1},
        {0x6030, 2,  1},
        {0x6030, 3,  2},
        {0x6030, 5,  2},
        {0x6030, 7,  1},
        {0x0000, 0,  1}, // gap
        {0x0000, 0,  6}, // gap
        {0x6030, 15, 1},
        {0x6030, 16, 1},
        {0x6030, 17, 16}};

ec_pdo_info_t EL3064_pdos[] = {
        // index,n_entries,*entries
        {0x1A00, 10, EL3064_pdo_entries + 0},
        {0x1A02, 10, EL3064_pdo_entries + 10},
        {0x1A04, 10, EL3064_pdo_entries + 20},
        {0x1A06, 10, EL3064_pdo_entries + 30}
};

ec_sync_info_t EL3064_syncs[] = {

        {0, EC_DIR_OUTPUT, 0, nullptr,         EC_WD_DISABLE},
        {1, EC_DIR_INPUT,  0, nullptr,         EC_WD_DISABLE},
        {2, EC_DIR_OUTPUT, 0, nullptr,         EC_WD_DISABLE},
        {3, EC_DIR_INPUT,  4, EL3064_pdos + 0, EC_WD_DISABLE},
        {0xff}};

ec_sync_info_t *EL3064::get_ec_sync_info_t_() {
    return EL3064_syncs;
}

ec_pdo_entry_reg_t *EL3064::Domain_regs(uint16_t position, uint32_t vendor_id, uint32_t product_code) {
    auto *ans = new ec_pdo_entry_reg_t[33]{
            {0, position, vendor_id, product_code, 0x6000, 1,  &pdo_offset.offset_6000[0], bit_position0},
            {0, position, vendor_id, product_code, 0x6000, 2,  &pdo_offset.offset_6000[0], bit_position1},
            {0, position, vendor_id, product_code, 0x6000, 3,  &pdo_offset.offset_6000[0], bit_position2},
            {0, position, vendor_id, product_code, 0x6000, 5,  &pdo_offset.offset_6000[0], bit_position4},
            {0, position, vendor_id, product_code, 0x6000, 7,  &pdo_offset.offset_6000[0], bit_position6},
            {0, position, vendor_id, product_code, 0x6000, 15, &pdo_offset.offset_6000[1], bit_position6},
            {0, position, vendor_id, product_code, 0x6000, 16, &pdo_offset.offset_6000[1], bit_position7},
            {0, position, vendor_id, product_code, 0x6000, 17, &pdo_offset.offset_6000[2], nullptr},

            {0, position, vendor_id, product_code, 0x6010, 1,  &pdo_offset.offset_6010[0], bit_position0},
            {0, position, vendor_id, product_code, 0x6010, 2,  &pdo_offset.offset_6010[0], bit_position1},
            {0, position, vendor_id, product_code, 0x6010, 3,  &pdo_offset.offset_6010[0], bit_position2},
            {0, position, vendor_id, product_code, 0x6010, 5,  &pdo_offset.offset_6010[0], bit_position4},
            {0, position, vendor_id, product_code, 0x6010, 7,  &pdo_offset.offset_6010[0], bit_position6},
            {0, position, vendor_id, product_code, 0x6010, 15, &pdo_offset.offset_6010[1], bit_position6},
            {0, position, vendor_id, product_code, 0x6010, 16, &pdo_offset.offset_6010[1], bit_position7},
            {0, position, vendor_id, product_code, 0x6010, 17, &pdo_offset.offset_6010[2], nullptr},

            {0, position, vendor_id, product_code, 0x6020, 1,  &pdo_offset.offset_6020[0], bit_position0},
            {0, position, vendor_id, product_code, 0x6020, 2,  &pdo_offset.offset_6020[0], bit_position1},
            {0, position, vendor_id, product_code, 0x6020, 3,  &pdo_offset.offset_6020[0], bit_position2},
            {0, position, vendor_id, product_code, 0x6020, 5,  &pdo_offset.offset_6020[0], bit_position4},
            {0, position, vendor_id, product_code, 0x6020, 7,  &pdo_offset.offset_6020[0], bit_position6},
            {0, position, vendor_id, product_code, 0x6020, 15, &pdo_offset.offset_6020[1], bit_position6},
            {0, position, vendor_id, product_code, 0x6020, 16, &pdo_offset.offset_6020[1], bit_position7},
            {0, position, vendor_id, product_code, 0x6020, 17, &pdo_offset.offset_6020[2], nullptr},

            {0, position, vendor_id, product_code, 0x6030, 1,  &pdo_offset.offset_6030[0], bit_position0},
            {0, position, vendor_id, product_code, 0x6030, 2,  &pdo_offset.offset_6030[0], bit_position1},
            {0, position, vendor_id, product_code, 0x6030, 3,  &pdo_offset.offset_6030[0], bit_position2},
            {0, position, vendor_id, product_code, 0x6030, 5,  &pdo_offset.offset_6030[0], bit_position4},
            {0, position, vendor_id, product_code, 0x6030, 7,  &pdo_offset.offset_6030[0], bit_position6},
            {0, position, vendor_id, product_code, 0x6030, 15, &pdo_offset.offset_6030[1], bit_position6},
            {0, position, vendor_id, product_code, 0x6030, 16, &pdo_offset.offset_6030[1], bit_position7},
            {0, position, vendor_id, product_code, 0x6030, 17, &pdo_offset.offset_6030[2], nullptr},
            {}};
    return ans;
}

void EL3064::config(int position) {
    std::string name = "EL3064_TxPDO_";
    name.append(std::to_string(position));
    int fd = shm_open(name.c_str(), O_CREAT | O_RDWR, 0666);
    ftruncate(fd, sizeof(EL3064_TxPDO));
    el3064TxPdo = (EL3064_TxPDO *) mmap(nullptr, sizeof(EL3064_TxPDO), PROT_WRITE, MAP_SHARED, fd, 0);
    printf("  mmap to %s\n", name.c_str());
}

void EL3064::read_data() {

    el3064TxPdo->status_limit_1_2[0] = EC_READ_BIT(domain_pd + pdo_offset.offset_6000[0], 3);
    el3064TxPdo->status_overrange[0] = EC_READ_BIT(domain_pd + pdo_offset.offset_6000[0], 1);
    el3064TxPdo->status_limit_2_1[0] = EC_READ_BIT(domain_pd + pdo_offset.offset_6000[0], 4);
    el3064TxPdo->status_limit_2_2[0] = EC_READ_BIT(domain_pd + pdo_offset.offset_6000[0], 5);
    el3064TxPdo->status_error[0] = EC_READ_BIT(domain_pd + pdo_offset.offset_6000[0], 6);
    el3064TxPdo->status_Txpdo_state[0] = EC_READ_BIT(domain_pd + pdo_offset.offset_6000[1], 6);
    el3064TxPdo->status_limit_1_1[0] = EC_READ_BIT(domain_pd + pdo_offset.offset_6000[0], 2);
    el3064TxPdo->status_Txpdo_Toggle[0] = EC_READ_BIT(domain_pd + pdo_offset.offset_6000[1], 7);
    el3064TxPdo->value[0] = EC_READ_U16(domain_pd + pdo_offset.offset_6000[2]);

    el3064TxPdo->status_underrange[1] = EC_READ_BIT(domain_pd + pdo_offset.offset_6010[0], 0);
    el3064TxPdo->status_underrange[0] = EC_READ_BIT(domain_pd + pdo_offset.offset_6000[0], 0);
    el3064TxPdo->status_overrange[1] = EC_READ_BIT(domain_pd + pdo_offset.offset_6010[0], 1);
    el3064TxPdo->status_limit_1_1[1] = EC_READ_BIT(domain_pd + pdo_offset.offset_6010[0], 2);
    el3064TxPdo->status_limit_1_2[1] = EC_READ_BIT(domain_pd + pdo_offset.offset_6010[0], 3);
    el3064TxPdo->status_limit_2_1[1] = EC_READ_BIT(domain_pd + pdo_offset.offset_6010[0], 4);
    el3064TxPdo->status_limit_2_2[1] = EC_READ_BIT(domain_pd + pdo_offset.offset_6010[0], 5);
    el3064TxPdo->status_error[1] = EC_READ_BIT(domain_pd + pdo_offset.offset_6010[0], 6);
    el3064TxPdo->status_Txpdo_state[1] = EC_READ_BIT(domain_pd + pdo_offset.offset_6010[1], 6);
    el3064TxPdo->status_Txpdo_Toggle[1] = EC_READ_BIT(domain_pd + pdo_offset.offset_6010[1], 7);
    el3064TxPdo->value[1] = EC_READ_U16(domain_pd + pdo_offset.offset_6010[2]);

    el3064TxPdo->status_underrange[2] = EC_READ_BIT(domain_pd + pdo_offset.offset_6020[0], 0);
    el3064TxPdo->status_overrange[2] = EC_READ_BIT(domain_pd + pdo_offset.offset_6020[0], 1);
    el3064TxPdo->status_limit_1_1[2] = EC_READ_BIT(domain_pd + pdo_offset.offset_6020[0], 2);
    el3064TxPdo->status_limit_1_2[2] = EC_READ_BIT(domain_pd + pdo_offset.offset_6020[0], 3);
    el3064TxPdo->status_limit_2_1[2] = EC_READ_BIT(domain_pd + pdo_offset.offset_6020[0], 4);
    el3064TxPdo->status_limit_2_2[2] = EC_READ_BIT(domain_pd + pdo_offset.offset_6020[0], 5);
    el3064TxPdo->status_error[2] = EC_READ_BIT(domain_pd + pdo_offset.offset_6020[0], 6);
    el3064TxPdo->status_Txpdo_state[2] = EC_READ_BIT(domain_pd + pdo_offset.offset_6020[1], 6);
    el3064TxPdo->status_Txpdo_Toggle[2] = EC_READ_BIT(domain_pd + pdo_offset.offset_6020[1], 7);
    el3064TxPdo->value[2] = EC_READ_U16(domain_pd + pdo_offset.offset_6020[2]);

    el3064TxPdo->status_underrange[3] = EC_READ_BIT(domain_pd + pdo_offset.offset_6030[0], 0);
    el3064TxPdo->status_overrange[3] = EC_READ_BIT(domain_pd + pdo_offset.offset_6030[0], 1);
    el3064TxPdo->status_limit_1_1[3] = EC_READ_BIT(domain_pd + pdo_offset.offset_6030[0], 2);
    el3064TxPdo->status_limit_1_2[3] = EC_READ_BIT(domain_pd + pdo_offset.offset_6030[0], 3);
    el3064TxPdo->status_limit_2_1[3] = EC_READ_BIT(domain_pd + pdo_offset.offset_6030[0], 4);
    el3064TxPdo->status_limit_2_2[3] = EC_READ_BIT(domain_pd + pdo_offset.offset_6030[0], 5);
    el3064TxPdo->status_error[3] = EC_READ_BIT(domain_pd + pdo_offset.offset_6030[0], 6);
    el3064TxPdo->status_Txpdo_state[3] = EC_READ_BIT(domain_pd + pdo_offset.offset_6030[1], 6);
    el3064TxPdo->status_Txpdo_Toggle[3] = EC_READ_BIT(domain_pd + pdo_offset.offset_6030[1], 7);
    el3064TxPdo->value[3] = EC_READ_U16(domain_pd + pdo_offset.offset_6030[2]);
}

void EL3064::send_data() {
    // donothing
}

void EL3064::process_data() {
#ifdef PRINT_DATA
    static int count = 1;
    count++;
    if (count == 1000) {
        count = 0;
        print();
    }
#endif //PRINT_DATA
}

void EL3064::print() {
    std::cout << "=====================================================" << std::endl;
    for (int i = 0; i < 4; i++) {
        std::cout << "port" << i << "data:\n"
                  << "  status_underrange: " << el3064TxPdo->status_underrange[i] << std::endl
                  << "  status_overrange: " << el3064TxPdo->status_overrange[i] << std::endl
                  << "  status_limit_1_1: " << el3064TxPdo->status_limit_1_1[i] << std::endl
                  << "  status_limit_1_2: " << el3064TxPdo->status_limit_1_2[i] << std::endl
                  << "  status_limit_2_1: " << el3064TxPdo->status_limit_2_1[i] << std::endl
                  << "  status_lixzmit_2_2: " << el3064TxPdo->status_limit_2_2[i] << std::endl
                  << "  status_error: " << el3064TxPdo->status_error[i] << std::endl
                  << "  status_Txpdo_state: " << el3064TxPdo->status_Txpdo_state[i] << std::endl
                  << "  status_Txpdo_Toggle: " << el3064TxPdo->status_Txpdo_Toggle[i] << std::endl
                  << "  value: " << el3064TxPdo->value[i] << std::endl;
    }
}


EL3064::~EL3064() {
    munmap(el3064TxPdo, sizeof(EL3064_TxPDO));
}
