import React from "react";

function AddNewCollectionTab({ closeHandle }) {
  return (
    <div className="container px-4 py-5">
      <h2>New endpoint</h2>
      <form></form>
      <button onClick={closeHandle}>Завершить</button>
      <button onClick={closeHandle}>Отменить</button>
    </div>
  );
}

export default AddNewCollectionTab;
