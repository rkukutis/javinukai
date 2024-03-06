import Button from "../Button";
import { useState } from "react";

export function Photo({ photo }) {
  const [isFullscreen, setIsFullscreen] = useState(false);
  return (
    <div>
      <img
        onClick={() => setIsFullscreen(!isFullscreen)}
        className="rounded hover:scale-105 hover:cursor-pointer"
        key={photo.id}
        src={photo.urlMiddle}
      />
      {isFullscreen && (
        <div className="flex justify-center items-center fixed top-0 left-0 w-full h-full backdrop-blur-xl backdrop-brightness-50 py-12">
          <div className="relative w-4/5">
            <Button
              onClick={() => setIsFullscreen(false)}
              extraStyle="absolute top-4 right-4"
            >
              Close
            </Button>
            <img className="" key={photo.id} src={photo.urlFull} />
          </div>
        </div>
      )}
    </div>
  );
}
