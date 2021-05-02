package app.categories;

public enum MorphType {
    MORPHISM,       // Simple, default type.
    MONOMORPHISM,   // Injective
    EPIMORPHISM,    // Surjective
    ISOMORPHISM,    // Bijective
    // These two other enumerations may not be needed
    ENDOMORPHISM,   // Source and target are the same object- not necessarely an identity
    AUTOMORPHISM    // A morphism both isomorphic and endomorphic
}
// There exist many other types of morphism, they are listed here: https://en.wikipedia.org/wiki/Morphism