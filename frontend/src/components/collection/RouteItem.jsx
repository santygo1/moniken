import React from "react";
import styles from "./RouteItem.module.css";

function RouteItem({ onClickHandler, ...props }) {
  return (
    <div className={styles.RouteItem} onClick={() => onClickHandler(props.id)}>
      <div>{props.method}</div>
      <h2>{props.endpoint}</h2>
      <p>{props.name}</p>
    </div>
  );
}

export default RouteItem;
