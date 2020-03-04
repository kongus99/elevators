open Bootstrap;
module IntSet = Belt.Set.Int;

let columnNumber = 3;

[@react.component]
let make = (~stops, ~floors, ~onButtonPress) => {
  let layout = Layout.init(floors, columnNumber);
  let pad = column => {
    let base = Layout.cellStartIndex(column, layout);
    <ButtonGroup vertical=true size="lg">
      {Array.init(
         Layout.cellSize(column, layout),
         i => {
           let floor = (base + i + 1) mod floors;
           <Button
             variant="primary"
             block=true
             active={IntSet.has(stops, floor)}
             onClick={onButtonPress(floor)}>
             {React.string(string_of_int(floor))}
           </Button>;
         },
       )
       |> React.array}
    </ButtonGroup>;
  };
  Array.init(columnNumber, pad) |> React.array;
};
