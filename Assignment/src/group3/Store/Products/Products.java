package group3.Store.Products;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Products {
    private List<Product> allProducts;
    private List<String> SKUList;
    private final List<Product> selectedProducts = new ArrayList<>();
    private List<String> animalTypes;

    public void loadAll() {
        allProducts = new ArrayList<>();
        SKUList = new ArrayList<>();
        animalTypes = loadAnimalTypes();
        for (String animalType : animalTypes) {
            try {
                File animalFile = new File("res/products/" + animalType + "/" + animalType + ".txt");
                if (!animalFile.isFile()) {
                    new File("res/products/" + animalType).mkdir();
                    animalFile.createNewFile();
                    continue;
                }

                BufferedReader br = new BufferedReader(new FileReader(animalFile));
                List<String> lines = br.lines().collect(Collectors.toList());
                if (lines.size() == 0) {
                    br.close();
                    continue;
                }

                for (int i = 0; i < lines.size(); i += 8) {
                    ProductInfo info = new ProductInfo(lines.get(i), lines.get(i + 1), lines.get(i + 2), lines.get(i + 3), lines.get(i + 4));
                    allProducts.add(new Product(info, animalType, Availability.valueOf(lines.get(i + 5)), lines.get(i + 6)));
                    SKUList.add(info.getSKU());
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> loadAnimalTypes() {
        List<String> animalTypes = null;
        try {
            animalTypes = Files.readAllLines(new File("res/categories.txt").toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return animalTypes;
    }

    public List<Product> getAllProducts() {
        return allProducts;
    }

    public List<String> getAnimalTypes() {
        return animalTypes;
    }

    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public List<String> getSKUList() {
        return SKUList;
    }

    public void setSelectedProducts(List<Product> selectedProducts) {
        this.selectedProducts.clear();
        this.selectedProducts.addAll(selectedProducts);
    }

    public void clearSelectedProducts() {
        selectedProducts.clear();
    }

    public void updateProduct(Product oldP, Product newP) {
        allProducts.set(allProducts.indexOf(oldP), newP);
    }

    public void removeProduct(Product product) {
        allProducts.remove(product);
    }
}
