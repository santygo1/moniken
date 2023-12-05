import React from "react";

function RouteItem(props) {
  return (
    <div className="feature col">
      <div>{props.method}</div>
      <h2>{props.endpoint}</h2>
      <p>{props.name}</p>
      <a href="#" className="icon-link">
        Редактировать
        <svg className="bi" width="1em" height="1em">
          <use xlinkHref="#chevron-right"></use>
        </svg>
      </a>
    </div>
  );
}

export default RouteItem;
