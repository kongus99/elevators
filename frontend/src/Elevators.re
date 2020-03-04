open Bootstrap;

[@bs.val] external fetch: string => Js.Promise.t('a) = "fetch";

let parseState = s =>
  Json.parseOrRaise(s) |> Json.Decode.(Elevator.decode |> array);

[@react.component]
let make = () => {
  let (state, setState) = React.useState(() => [||]);

  let fetchState = () => {
    Js.Promise.(
      fetch("http://localhost:8080/rest/v1/state")
      |> then_(response => {response##text()})
      |> then_(jsonResponse => {
           setState(_previousState => parseState(jsonResponse));
           Js.Promise.resolve();
         })
      |> catch(_err => {
           setState(_previousState => [||]);
           Js.Promise.resolve();
         })
      |> ignore
    );
  };

  React.useEffect0(() => {
    let timerId = Js.Global.setInterval(() => fetchState(), 1000);
    Some(() => Js.Global.clearInterval(timerId));
  });

  <Container fluid=true>
    <Row>
      <Col lg="2"> <Floors /> </Col>
      {state
       |> Array.map(s => <Col lg="2"> <Elevator state=s /> </Col>)
       |> React.array}
    </Row>
  </Container>;
};
