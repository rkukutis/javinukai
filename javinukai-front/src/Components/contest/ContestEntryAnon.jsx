import formatTimestap from "../../utils/formatTimestap";
import { useState } from "react";
import { Photo } from "../photo/Photo";

export default function ContestEntryAnon({ entry }) {
  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <div className="border-2 p-4 rounded-md">
      <div className="">
        {entry.images.map((image) => (
          <Photo key={image.id} photo={image} />
        ))}
      </div>
    </div>
  );
}
