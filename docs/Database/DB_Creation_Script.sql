-- MySQL Script generated by MySQL Workbench
-- Wed Apr 21 18:58:01 2021
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema cab302_eTrade
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cab302_eTrade
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cab302_eTrade` DEFAULT CHARACTER SET utf8 ;
USE `cab302_eTrade` ;

-- -----------------------------------------------------
-- Table `cab302_eTrade`.`assets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cab302_eTrade`.`assets` (
  `asset_id` INT NOT NULL,
  `asset_name` VARCHAR(16) NOT NULL,
  `asset_description` VARCHAR(256) NULL DEFAULT NULL,
  PRIMARY KEY (`asset_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cab302_eTrade`.`organisations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cab302_eTrade`.`organisations` (
  `organisation_id` INT NOT NULL AUTO_INCREMENT,
  `organisation_name` VARCHAR(16) NOT NULL,
  `credits` DECIMAL(2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`organisation_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cab302_eTrade`.`stock`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cab302_eTrade`.`stock` (
  `organisation_id` INT NOT NULL,
  `asset_id` INT NOT NULL,
  `asset_quantity` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`organisation_id`, `asset_id`),
  CONSTRAINT `asset`
    FOREIGN KEY (`asset_id`)
    REFERENCES `cab302_eTrade`.`assets` (`asset_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `stock_organisation`
    FOREIGN KEY (`organisation_id`)
    REFERENCES `cab302_eTrade`.`organisations` (`organisation_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `asset_idx` ON `cab302_eTrade`.`stock` (`asset_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `cab302_eTrade`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cab302_eTrade`.`orders` (
  `order_id` INT NOT NULL AUTO_INCREMENT,
  `order_type` ENUM('buy', 'sell') NOT NULL,
  `organisation_id` INT NOT NULL,
  `asset_id` INT NOT NULL,
  `placed_quantity` INT NOT NULL DEFAULT 0,
  `resolved_quantity` INT NOT NULL DEFAULT 0,
  `price` DECIMAL(2) NOT NULL,
  `order_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `finished_date` DATETIME NULL DEFAULT NULL,
  `status` ENUM('placed', 'finished', 'cancelled') NOT NULL DEFAULT 'placed',
  PRIMARY KEY (`order_id`),
  CONSTRAINT `buy_organisation`
    FOREIGN KEY (`organisation_id` , `asset_id`)
    REFERENCES `cab302_eTrade`.`stock` (`organisation_id` , `asset_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `organisation_idx` ON `cab302_eTrade`.`orders` (`organisation_id` ASC, `asset_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `cab302_eTrade`.`transactions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cab302_eTrade`.`transactions` (
  `transaction_id` INT NOT NULL AUTO_INCREMENT,
  `sell_order_id` INT NOT NULL,
  `buy_order_id` INT NOT NULL,
  `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `quantity` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`transaction_id`),
  CONSTRAINT `resolved_orders`
    FOREIGN KEY (`buy_order_id` , `sell_order_id`)
    REFERENCES `cab302_eTrade`.`orders` (`order_id` , `order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `buy_idx` ON `cab302_eTrade`.`transactions` (`buy_order_id` ASC, `sell_order_id` ASC) INVISIBLE;


-- -----------------------------------------------------
-- Table `cab302_eTrade`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cab302_eTrade`.`users` (
  `username` VARCHAR(16) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  `user_type` ENUM('user', 'admin') NOT NULL DEFAULT 'user',
  `organisation_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`username`),
  CONSTRAINT `user_organisaion`
    FOREIGN KEY (`organisation_id`)
    REFERENCES `cab302_eTrade`.`organisations` (`organisation_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `organisaion_idx` ON `cab302_eTrade`.`users` (`organisation_id` ASC) VISIBLE;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `cab302_eTrade`.`users`
-- -----------------------------------------------------
START TRANSACTION;
USE `cab302_eTrade`;
INSERT INTO `cab302_eTrade`.`users` (`username`, `password`, `user_type`, `organisation_id`) VALUES ('admin', 'root', 'admin', NULL);

COMMIT;

