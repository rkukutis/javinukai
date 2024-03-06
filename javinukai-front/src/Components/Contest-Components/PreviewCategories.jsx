import React, { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import EditCategoryModal from "./EditCategoryModal";

function PreviewCategories() {
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [sortBy, setSortBy] = useState("recent");

  const { data: categories, isLoading, error } = useQuery({
    queryKey: ["categories"],
    queryFn: async () => {
      const response = await axios.get(
        `${import.meta.env.VITE_BACKEND}/api/v1/categories`, {withCredentials: true}
      );
      return response.data.content;
    }
  });

  const filteredCategories = categories?.filter((category) =>
    category.categoryName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleEditCategory = (categoryId) => {
    const categoryToEdit = categories.find(
      (category) => category.id === categoryId
    );
    setSelectedCategory(categoryToEdit);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedCategory(null);
  };

  if (isLoading) {
    return <div className="text-center mt-4">Loading...</div>;
  }

  if (error) {
    return <div className="text-center mt-4">Error: {error.message}</div>;
  }

  const sortedCategories = filteredCategories.sort((a, b) => {
    if (sortBy === "recent") {
      return new Date(b.createdAt) - new Date(a.createdAt);
    } else if (sortBy === "submissions") {
      return b.totalSubmissions - a.totalSubmissions;
    }
    return 0;
  });

  return (
    <div className="p-4">
      <h2 className="text-2xl font-semibold mb-4">Categories</h2>
      <div className="flex justify-between mb-4">
        <div>
          <input
            type="text"
            placeholder="Search by name..."
            className="border border-gray-300 rounded-md px-3 py-1 mr-2"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            className="border border-gray-300 rounded-md px-3 py-1"
          >
            <option value="recent">Recently Added</option>
            <option value="submissions">Most Total Submissions</option>
          </select>
        </div>
      </div>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        {sortedCategories.map((category) => (
          <div key={category.id} className="mb-4 border-b pb-4">
            <div>
              <strong>Name:</strong> {category.categoryName}
            </div>
            <div>
              <strong>Description:</strong> {category.description}
            </div>
            <div>
              <strong>Total Submissions:</strong> {category.totalSubmissions}
            </div>
            <div>
              <strong>Type:</strong> {category.type}
            </div>
            <div>
              <strong>Created At:</strong>{" "}
              {new Date(category.createdAt).toLocaleString()}
            </div>
            <div>
              <strong>Modified At:</strong>{" "}
              {new Date(category.modifiedAt).toLocaleString()}
            </div>
            <button
              onClick={() => handleEditCategory(category.id)}
              className="mt-2 bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 px-4 rounded"
            >
              Edit
            </button>
          </div>
        ))}
      </div>

      {showModal && (
        <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-4 rounded shadow-lg">
            <EditCategoryModal
              category={selectedCategory}
              onClose={handleCloseModal}
            />
          </div>
        </div>
      )}
    </div>
  );
}

export default PreviewCategories;
