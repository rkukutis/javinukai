import cat1 from "../assets/cat1.jpg";
import cat2 from "../assets/cat2.jpg";
import cat3 from "../assets/cat3.jpg";
import Button from "../Components/Button";

function HomePage() {
  return (
    <div className="flex flex-col items-center ">
      <div className="flex items-center mx-4 mb-4 justify-center">
        <img
          src={cat1}
          alt="cat1"
          className="max-w-full max-h-full object-cover"
          style={{ maxHeight: "80vh" }}
        />
        <div className="fle items-center justify-center p-5">
          <p className="ml-4">contest 2020</p>
          <p>Description</p>
          <Button>TEST BUTTON</Button>
        </div>
      </div>

      <div className="flex items-center mx-4 mb-4">
        <img
          src={cat2}
          alt="cat2"
          className="max-w-full max-h-full object-cover"
          style={{ maxHeight: "80vh" }}
        />
        <div className="fle items-center justify-center p-5">
          <p className="ml-4">contest 2091</p>
          <p>Description</p>
          <Button>TEST BUTTON</Button>
        </div>
      </div>

      <div className="flex items-center mx-4">
        <img
          src={cat3}
          alt="cat3"
          className="max-w-full max-h-full object-cover"
          style={{ maxHeight: "80vh" }}
        />
        <div className="fle items-center justify-center p-5">
          <p className="ml-4">contest 2018</p>
          <p>Description</p>
          <Button>TEST BUTTON</Button>
        </div>
      </div>
    </div>
  );
}

export default HomePage;
