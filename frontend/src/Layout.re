type t = array(int);

let calculate = (max, totalSize) => {
  let size =
    float_of_int(max) /. float_of_int(totalSize) |> ceil |> int_of_float;
  Array.init(totalSize, i =>
    if (i + 1 == totalSize) {
      max mod size;
    } else {
      size;
    }
  );
};

let init = (max, cellSize) => calculate(max, cellSize);

let cellSize = (index, layout) => layout[index];

let cellStartIndex = (index, layout) =>
  Array.mapi(
    (i, v) =>
      if (i < index) {
        v;
      } else {
        0;
      },
    layout,
  )
  |> Array.fold_left((+), 0);
