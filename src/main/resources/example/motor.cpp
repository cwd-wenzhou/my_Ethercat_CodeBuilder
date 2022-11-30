//
// Created by imc on 22-11-27.
//
#include "include/motor.h"

ec_pdo_entry_info_t MOTOR_pdo_entries[] = {
        /*RxPdo 0x1600*/
        {0x6040, 0x00, 16},//Controlword
        {0x6060, 0x00, 8},//Mode of Operation
        {0x607A, 0x00, 32},//Target Position
        {0x60FF, 0x00, 32},//Target Velocity
        {0x6071, 0x00, 16},//Target Torque
        /*TxPdo 0x1A00*/
        {0x6041, 0x00, 16},//Statusword
        {0x6061, 0x00, 8},//Mode of Operation Display
        {0x6064, 0x00, 32},//Position Actual Value
        {0x606C, 0x00, 32},//Velocity Actual Value
        {0x6077, 0x00, 16},//Torque Actual Value
};

ec_pdo_info_t MOTOR_pdos[] = {// RxPdo
        {0x1600, 5, MOTOR_pdo_entries + 0},
        // TxPdo
        {0x1a00, 5, MOTOR_pdo_entries + 5}};
ec_sync_info_t MOTOR_syncs[] = {{0, EC_DIR_OUTPUT, 0, nullptr,        EC_WD_DISABLE},
                                {1, EC_DIR_INPUT,  0, nullptr,        EC_WD_DISABLE},
                                {2, EC_DIR_OUTPUT, 1, MOTOR_pdos + 0, EC_WD_DISABLE},
                                {3, EC_DIR_INPUT,  1, MOTOR_pdos + 1, EC_WD_DISABLE},
                                {0xFF}};

ec_sync_info_t *MOTOR::get_ec_sync_info_t_() {
    return MOTOR_syncs;
}

void MOTOR::read_data() {
    motorTxPdo->status_word = EC_READ_U16(domain_pd + pdo_offset.offset_6041);
    motorTxPdo->mode_of_Operation_Display = EC_READ_U8(domain_pd + pdo_offset.offset_6061);
    motorTxPdo->current_Position = EC_READ_S32(domain_pd + pdo_offset.offset_6064);
    motorTxPdo->current_Velocity = EC_READ_S32(domain_pd + pdo_offset.offset_606c);
    motorTxPdo->current_torque = EC_READ_S16(domain_pd + pdo_offset.offset_6077);

}


void MOTOR::process_data() {
   //print();
}

void MOTOR::send_data() {
    EC_WRITE_U16(domain_pd + pdo_offset.offset_6040, motorRxPdo->control_word);
    EC_WRITE_U8(domain_pd + pdo_offset.offset_6060, motorRxPdo->mode_of_Operation);
    EC_WRITE_S32(domain_pd + pdo_offset.offset_607a, motorRxPdo->target_position);
    EC_WRITE_S32(domain_pd + pdo_offset.offset_60ff, motorRxPdo->target_velocity);
    EC_WRITE_S16(domain_pd + pdo_offset.offset_6071, motorRxPdo->target_torque);
}

void MOTOR::print() {
    printf("\033[?25l");
    printf("%u                  %u               %10u                 %10u                 %10u\n ",
           motorRxPdo->mode_of_Operation, motorRxPdo->control_word, motorRxPdo->target_velocity,
           motorRxPdo->target_position, motorRxPdo->target_torque);
    printf("%u                  %u               %10u                 %10u                 %10u\n ",
           motorTxPdo->mode_of_Operation_Display, motorTxPdo->status_word, motorTxPdo->current_Velocity,
           motorTxPdo->current_Position, motorTxPdo->current_torque);
    printf("\033[2A");
}

ec_pdo_entry_reg_t *MOTOR::Domain_regs(uint16_t position, uint32_t vendor_id, uint32_t product_code) {
    auto *ans = new ec_pdo_entry_reg_t[11]{
            {0, position, vendor_id, product_code, 0x6040, 0, &pdo_offset.offset_6040},
            {0, position, vendor_id, product_code, 0x6060, 0, &pdo_offset.offset_6060},
            {0, position, vendor_id, product_code, 0x607a, 0, &pdo_offset.offset_607a},
            {0, position, vendor_id, product_code, 0x60ff, 0, &pdo_offset.offset_60ff},
            {0, position, vendor_id, product_code, 0x6071, 0, &pdo_offset.offset_6071},

            {0, position, vendor_id, product_code, 0x6041, 0, &pdo_offset.offset_6041},
            {0, position, vendor_id, product_code, 0x6061, 0, &pdo_offset.offset_6061},
            {0, position, vendor_id, product_code, 0x6064, 0, &pdo_offset.offset_6064},
            {0, position, vendor_id, product_code, 0x606c, 0, &pdo_offset.offset_606c},
            {0, position, vendor_id, product_code, 0x6077, 0, &pdo_offset.offset_6077},
            {}
    };
    return ans;
}

void MOTOR::config(int position) {
    std::string txpdo_name = "MOTOR_TxPDO_";
    txpdo_name.append(std::to_string(position));
    int fd = shm_open(txpdo_name.c_str(), O_CREAT | O_RDWR, 0666);
    ftruncate(fd, sizeof(MOTOR_TxPDO));
    motorTxPdo = (MOTOR_TxPDO *) mmap(nullptr, sizeof(MOTOR_TxPDO), PROT_WRITE, MAP_SHARED, fd, 0);
    printf("  mmap to %s\n", txpdo_name.c_str());

    std::string rxpdo_name = "MOTOR_RxPDO_";
    rxpdo_name.append(std::to_string(position));
    fd = shm_open(rxpdo_name.c_str(), O_CREAT | O_RDWR, 0666);
    ftruncate(fd, sizeof(MOTOR_RxPDO));
    motorRxPdo = (MOTOR_RxPDO *) mmap(nullptr, sizeof(MOTOR_RxPDO), PROT_WRITE|PROT_READ, MAP_SHARED, fd, 0);
    printf("  mmap to %s\n", rxpdo_name.c_str());
}

MOTOR::~MOTOR() {
    munmap(motorRxPdo, sizeof(MOTOR_RxPDO));
    munmap(motorTxPdo, sizeof(MOTOR_TxPDO));
}