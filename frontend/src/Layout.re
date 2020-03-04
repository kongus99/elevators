type limits = {
  max: int,
  cellSize: int,
};

type layout = {
  limits,
  sizes: array(int),
};

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

let init = (max, cellSize) => {
  limits: {
    max,
    cellSize,
  },
  sizes: calculate(max, cellSize),
};

let cellSize = (index, layout) => layout.sizes[index];

let cellStartIndex = (index, layout) =>
  Array.mapi(
    (i, v) =>
      if (i < index) {
        v;
      } else {
        0;
      },
    layout.sizes,
  )
  |> Array.fold_left((+), 0);
