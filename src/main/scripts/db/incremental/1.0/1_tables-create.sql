CREATE TABLE `STORE` (
  `ID` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `NUMBER` INT(10) NOT NULL,
  `NETWORK` VARCHAR(20) NOT NULL,
  `START_IP` INT(10) NOT NULL,
  `END_IP` INT(10) NOT NULL,
  `ISP_IP` VARCHAR(20) NOT NULL,
  `ISP_PORT` VARCHAR(20) NOT NULL,
  `PROTOCOL` VARCHAR(20) NOT NULL,
  `DIVISION` VARCHAR(20) NOT NULL,
  `VERSION` BIGINT NOT NULL DEFAULT 1,
  `CREATED_BY` BIGINT,
  `CREATED_DATE` BIGINT,
  `LAST_MODIFIED_BY` BIGINT,
  `LAST_MODIFIED_DATE` BIGINT
);

CREATE TABLE `LANE_TYPE` (
  `ID` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `TYPE` VARCHAR(20) NOT NULL,
  `VERSION` BIGINT NOT NULL DEFAULT 1,
  `CREATED_BY` BIGINT,
  `CREATED_DATE` BIGINT,
  `LAST_MODIFIED_BY` BIGINT,
  `LAST_MODIFIED_DATE` BIGINT
);

CREATE TABLE `LANE` (
  `ID` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `NUMBER` INT(20) NOT NULL,
  `TYPE_ID` BIGINT REFERENCES `LANE_TYPE` (`ID`),
  `STORE_ID` BIGINT REFERENCES `STORE` (`ID`),
  `ACTIVE` CHAR(1) NOT NULL DEFAULT 1,
  `VERSION` BIGINT NOT NULL DEFAULT 1,
  `CREATED_BY` BIGINT,
  `CREATED_DATE` BIGINT,
  `LAST_MODIFIED_BY` BIGINT,
  `LAST_MODIFIED_DATE` BIGINT
);

CREATE TABLE `EMPLOYEE` (
  `ID` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `OPERATOR_NO` INT(4) NOT NULL,
  `FIRST_NAME` VARCHAR(20) NOT NULL,
  `LAST_NAME` VARCHAR(3) NOT NULL,
  `ACTIVE` CHAR(1) NOT NULL DEFAULT 1,
  `REQUIRES_AUDIT` CHAR(1) NOT NULL DEFAULT 0,
  `STORE_ID` BIGINT NOT NULL REFERENCES `STORE` (`ID`),
  `VERSION` BIGINT NOT NULL DEFAULT 1,
  `CREATED_BY` BIGINT,
  `CREATED_DATE` BIGINT,
  `LAST_MODIFIED_BY` BIGINT,
  `LAST_MODIFIED_DATE` BIGINT,
  UNIQUE KEY (STORE_ID, OPERATOR_NO)
);

CREATE TABLE `LANE_HISTORY` (
  `ID` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `LANE_ID` BIGINT REFERENCES `LANE` (`ID`),
  `EMPLOYEE_ID` BIGINT REFERENCES `EMPLOYEE` (`ID`),
  `STORE_ID` BIGINT REFERENCES `STORE` (`ID`),
  `DATE` DATE NOT NULL,
  `START_TIME` BIGINT,
  `END_TIME` BIGINT,
  `VERSION` BIGINT NOT NULL DEFAULT 1,
  `CREATED_BY` BIGINT,
  `CREATED_DATE` BIGINT,
  `LAST_MODIFIED_BY` BIGINT,
  `LAST_MODIFIED_DATE` BIGINT
);

CREATE TABLE `AUDIT` (
  `ID` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `LANE_ID` BIGINT REFERENCES `LANE` (`ID`),
  `EMPLOYEE_ID` BIGINT REFERENCES `EMPLOYEE` (`ID`),
  `STORE_ID` BIGINT REFERENCES `STORE` (`ID`),
  `DATE` DATE NOT NULL,
  `CASH_POSITION` DOUBLE NOT NULL,
  `CASH_EXPECTED` DOUBLE NOT NULL,
  `CASH_ACTUAL` DOUBLE NOT NULL,
  `CASH_DIFFERENCE` DOUBLE NOT NULL,
  `CASH_OVER_UNDER` DOUBLE NOT NULL,
  `CHECK_POSITION` DOUBLE,
  `CHECK_EXPECTED` DOUBLE,
  `CHECK_ACTUAL` DOUBLE,
  `CHECK_DIFFERENCE` DOUBLE,
  `CHECK_OVER_UNDER` DOUBLE,
  `NOTES` VARCHAR(1024),
  `SLIP` LONGTEXT,
  `EMP_SIGNATURE` LONGTEXT,
  `MGR_SIGNATURE` LONGTEXT,
  `POSITION` INT(1),
  `VERSION` BIGINT NOT NULL DEFAULT 1,
  `CREATED_BY` BIGINT,
  `CREATED_DATE` BIGINT,
  `LAST_MODIFIED_BY` BIGINT,
  `LAST_MODIFIED_DATE` BIGINT
);