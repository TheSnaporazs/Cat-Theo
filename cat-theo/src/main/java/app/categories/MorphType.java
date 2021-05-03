package app.categories;

public enum MorphType {
    MORPHISM,       // Simple, default type.
    MONOMORPHISM,   // Injective
    EPIMORPHISM,    // Surjective
    ISOMORPHISM,    // Bijective
    IDENTITY
}
// There exist many other types of morphism, they are listed here: https://en.wikipedia.org/wiki/Morphism