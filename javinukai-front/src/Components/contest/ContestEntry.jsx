import formatTimestap from "../../utils/formatTimestap";
import { useState } from "react";
import { Photo } from "../photo/Photo";

export default function ContestEntry({ entry, index }) {
  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <div className="border-2 p-4 rounded-md">
      <div
        className="flex space-x-5"
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <h1>Entry #{index + 1}</h1>
        <h1>{entry.name}</h1>
        <p>{entry.description}</p>
        <p>Added: {formatTimestap(entry.createdAt)}</p>
        <p>Photos: {entry.images.length}</p>
      </div>
      {isExpanded && (
        <div className="flex flex-col xl:grid xl:grid-cols-3 w-full gap-6 my-4">
          {entry.images.map((image) => (
            <Photo key={image.id} photo={image} />
          ))}
        </div>
      )}
    </div>
  );
}
